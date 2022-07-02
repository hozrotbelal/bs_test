package com.example.bs_test.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.*
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.location.LocationManager
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcel
import android.os.SystemClock
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import androidx.core.os.ParcelCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.bs_test.BuildConfig
import com.example.bs_test.R

import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import com.example.bs_test.domain.result.Event
import com.example.bs_test.util.isEmpty

class EmptyClass

/** Convenience for callbacks/listeners whose return value indicates an event was consumed. */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

/**
 * Allows calls like

 * `viewGroup.inflate(R.layout.foo)`
 */
fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}

fun Context.addThousanSeparetor(number: Double): String {

    try {
        val takaAmount = if (number.isNotNull()) number else 0.0
        val formatter = DecimalFormat("##,##,##,##0.00")
        val formattedTaka = formatter.format(takaAmount)
        return "৳ $formattedTaka"
    } catch (e: Exception) {
        return "৳ 0"
    }
//    return NumberFormat.getIntegerInstance().format(number)+"/-"
}

fun addThousanSeparetor(number: Double): String {

    return "৳ " + NumberFormat.getIntegerInstance().format(number)
}


/**
 * Allows calls like
 *
 * `supportFragmentManager.inTransaction { add(...) }`
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

// region ViewModels

/**
 * For Actvities, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(this, provider).get(VM::class.java)

/**
 * For Fragments, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(this, provider).get(VM::class.java)

/**
 * Like [Fragment.viewModelProvider] for Fragments that want a [ViewModel] scoped to the Activity.
 */
inline fun <reified VM : ViewModel> Fragment.activityViewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(requireActivity(), provider).get(VM::class.java)

/**
 * Like [Fragment.viewModelProvider] for Fragments that want a [ViewModel] scoped to the parent
 * Fragment.
 */
inline fun <reified VM : ViewModel> Fragment.parentViewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(parentFragment!!, provider).get(VM::class.java)

// endregion
// region Parcelables, Bundles

/** Write a boolean to a Parcel. */
fun Parcel.writeBooleanUsingCompat(value: Boolean) = ParcelCompat.writeBoolean(this, value)

/** Read a boolean from a Parcel. */
fun Parcel.readBooleanUsingCompat() = ParcelCompat.readBoolean(this)

// endregion
// region LiveData

/** Uses `Transformations.map` on a LiveData */
fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}

///** Uses `Transformations.switchMap` on a LiveData */
//fun <X, Y> LiveData<X>.switchMap(body: (X) -> LiveData<Y>): LiveData<Y> {
//    return Transformations.switchMap(this, body)
//}

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

fun <T> MutableLiveData<T>.toggleTwoValue(first: T, second: T) {
    this.value = when (this.value) {
        first -> second
        else -> first
    }.checkAllMatched
}

fun <A, B, Result> LiveData<A>.combine(
    other: LiveData<B>,
    combiner: (A, B) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other.value
        if (b != null) {
            result.postValue(combiner(a, b))
        }
    }
    result.addSource(other) { b ->
        val a = this@combine.value
        if (a != null) {
            result.postValue(combiner(a, b))
        }
    }
    return result
}

/**
 * Combines this [LiveData] with other two [LiveData]s using the specified [combiner] and returns
 * the result as a [LiveData].
 */
fun <A, B, C, Result> LiveData<A>.combine(
    other1: LiveData<B>,
    other2: LiveData<C>,
    combiner: (A, B, C) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other1.value
        val c = other2.value
        if (b != null && c != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    result.addSource(other1) { b ->
        val a = this@combine.value
        val c = other2.value
        if (a != null && c != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    result.addSource(other2) { c ->
        val a = this@combine.value
        val b = other1.value
        if (a != null && b != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    return result
}


//inline fun <T> LiveData<T>.filter(crossinline predicate: (T?) -> Boolean): LiveData<T> {
//    val result = MediatorLiveData<T>()
//    result.addSource(this) { if (predicate(it)) result.value = it }
//    return result
//}

/**
 * Helper to force a when statement to assert all options are matched in a when statement.
 *
 * By default, Kotlin doesn't care if all branches are handled in a when statement. However, if you
 * use the when statement as an expression (with a value) it will force all cases to be handled.
 *
 * This helper is to make a lightweight way to say you meant to match all of them.
 *
 * Usage:
 *
 * ```
 * when(sealedObject) {
 *     is OneType -> //
 *     is AnotherType -> //
 * }.checkAllMatched
 */
val <T> T.checkAllMatched: T
    get() = this

// region UI utils

// endregion

/**
 * Helper to throw exceptions only in Debug builds, logging a warning otherwise.
 */
fun exceptionInDebug(t: Throwable) {
    if (BuildConfig.DEBUG) {
        throw t
    } else {
        Timber.e(t)
    }
}


fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, ViewPaddingState) -> Unit) {
    // Create a snapshot of the view's padding state
    val paddingState = createStateForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, paddingState)
        insets
    }
    requestApplyInsetsWhenAttached()
}

/**
 * Call [View.requestApplyInsets] in a safe away. If we're attached it calls it straight-away.
 * If not it sets an [View.OnAttachStateChangeListener] and waits to be attached before calling
 * [View.requestApplyInsets].
 */
fun View.requestApplyInsetsWhenAttached() {
    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            isAttachedToWindow
        else
            ViewCompat.isAttachedToWindow(this)
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            requestApplyInsets()
        } else {
            ViewCompat.requestApplyInsets(this)
        }
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    v.requestApplyInsets()
                } else {
                    ViewCompat.requestApplyInsets(v)
                }
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun Context.sharedPreferences(name: String): SharedPreferences {
    val context = this.applicationContext
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    return EncryptedSharedPreferences.create(
        context,
        name,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}


data class ViewPaddingState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
)

private fun createStateForView(view: View) =
    ViewPaddingState(
        view.paddingLeft,
        view.paddingTop,
        view.paddingRight,
        view.paddingBottom,
        view.paddingLeft,
        view.paddingRight
    )

fun Activity.showDialog(
    cancelable: Boolean = true, cancelableTouchOutside: Boolean = true,
    builderFunction: AlertDialog.Builder.() -> Any
) {
    val builder = AlertDialog.Builder(this)
    builder.builderFunction()
    val dialog = builder.create()
    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(cancelableTouchOutside)
    dialog.show()
}

fun Fragment.showDialog(
    cancelable: Boolean = true, cancelableTouchOutside: Boolean = true,
    builderFunction: AlertDialog.Builder.() -> Any
) {
    this.context?.let {
        it.showDialog(cancelable, cancelableTouchOutside, builderFunction)
    }
}

fun Context.eventMessage(@StringRes id: Int) = Event(this.getString(id))

fun Context.showDialog(
    cancelable: Boolean = true, cancelableTouchOutside: Boolean = true,
    builderFunction: AlertDialog.Builder.() -> Any
) {
    val builder = AlertDialog.Builder(this)
    builder.builderFunction()
    val dialog = builder.create()
    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(cancelableTouchOutside)
    dialog.show()
}


fun AlertDialog.Builder.positiveButton(
    text: String,
    handleClick: () -> Unit = {}
) {
    this.setPositiveButton(text, { dialogInterface, i -> handleClick() })
}


fun AlertDialog.Builder.negativeButton(
    text: String,
    handleClick: (i: Int) -> Unit = {}
) {
    this.setNegativeButton(text, { dialogInterface, i -> handleClick(i) })
}

fun AlertDialog.Builder.neutralButton(text: String, handleClick: (i: Int) -> Unit = {}) {
    this.setNeutralButton(text, { dialogInterface, i -> handleClick(i) })
}

fun Context.toastShort(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.toastShort(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.toastLong(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Context.toastLong(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_LONG).show()


fun Context.getColorValue(@ColorRes id: Int) = ContextCompat.getColor(this, id)


val Context.downloadManager: DownloadManager
    get() = (this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?)!!

val Context.connectivityManager: ConnectivityManager
    get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)!!


val Context.inputMethodManager: InputMethodManager
    get() = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val View.inputMethodManager: InputMethodManager
    get() = this.context.inputMethodManager


val View.showKeyboard: Boolean
    get() = this.inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_FORCED)

val View.dismissKeyboard: Boolean
    get() = this.inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)

private fun dismissKeyboard(view: View) =
    view.inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)


val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

val Context.dividerItemDecorationVertical
    get() = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

val View.layoutInflater: LayoutInflater
    get() = this.context.layoutInflater


@SuppressLint("ResourceType")
inline fun View.snack(
    @StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG
) {
    snack(resources.getString(messageRes), length)
}

@SuppressLint("ResourceType")
inline fun View.snack(
    @StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

@SuppressLint("ResourceType")
fun Snackbar.action(@IntegerRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

fun Any?.isNull() = this == null
fun Any?.isNotNull() = !isNull()

inline fun <T : Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}

inline fun <T : Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
    return if (elements.all { it != null }) {
        elements.filterNotNull()
    } else {
        closure()
    }
}

fun fromHtml(data: String): CharSequence = HtmlCompat.fromHtml(data, FROM_HTML_MODE_COMPACT)

fun Fragment.navigate(direction: NavDirections) = this.findNavController().navigate(direction)
fun Fragment.navigateUp() = this.findNavController().navigateUp()
fun Activity.navigate(@IdRes viewId: Int, direction: NavDirections) =
    this.findNavController(viewId).navigate(direction)


inline fun <reified T : Enum<T>> Intent.putEnumExtra(victim: T): Intent =
    putExtra(T::class.qualifiedName, victim.ordinal)

inline fun <reified T : Enum<T>> Intent.getEnumExtra(): T? =
    getIntExtra(T::class.qualifiedName, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants?.get(it) }


val BANGLA
    get() = Locale("bn", "BD")

val ENGLISH
    get() = Locale("en", "US")


/*========= Hide Keyboard ===============*/
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showeKeyboard() {
    view?.let { activity?.showKeypad(it) }


}

fun Fragment.keypadShow() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

public val onSoftKeyboardDonePress =
    View.OnKeyListener { v, keyCode, event ->
        if (event.keyCode === KeyEvent.KEYCODE_ENTER) {
        }
        false
    }
public val onSoftKeyboardNextPress =
    View.OnKeyListener { v, keyCode, event ->
        if (event.keyCode === KeyEvent.KEYCODE_MEDIA_NEXT) {
        }
        false
    }

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeypad(view: View) {
    val imm =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}


fun getInt(value: String): Int {
    if (value != null && value.trim().length > 0)
        return value.toInt()
    else return 0
}

fun getWholeNumber(value: Double): String {

    Timber.e(value.toString() + "-")
    if (value - value.toInt() == 0.0) {
        return value.toInt().toString()


    } else
        return return String.format("%.2f", value)

}





fun changeStatusBarColor(context: Context, statusBarColor: Int) {
    val activity = context as Activity
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window: Window = activity.window
        window.statusBarColor = ContextCompat.getColor(context, statusBarColor);
        //window.setNavigationBarColor(Color.BLUE);
        val decor = activity.window.decorView
        //decor.systemUiVisibility = View.SYSTE
        //decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }

}

fun getOtpFromMessage(message: String): String {
    // This will match any 4 digit number in the message
    val pattern: Pattern = Pattern.compile(("\\d{4}"))
    var matcher: Matcher? = null

    val domain: String? = message.toLowerCase().substringBefore(" is your")
    matcher = pattern.matcher(domain)

    if (matcher.find()) {
        Timber.e("otp is " + domain.toString())
        return matcher.group(0)!!.toString()
    } else {
        Timber.e("otp is fail" + domain.toString())

        return ""
    }

}

fun hashCheck(message: String, hash: String): Boolean {
    // This will match any 4 digit number in the message
    Timber.e((if (message.contains(hash)) "match" else "note match") + firstWord(message))
    val hashSelected: String? = message.substringAfter(" HASH: ")
    if (hashSelected?.trim().equals(hash)) {
        Timber.e("hash matched is " + hashSelected)
        return true
    } else {
        Timber.e("hash not match " + hashSelected)

        return false
    }

}

fun firstWord(input: String): String? {
    return input.split(" ").toTypedArray()[0] // Create array of words and return the 0th word
}

fun View.visible(animate: Boolean = true) {
    if (animate) {
        animate().alpha(1f).setDuration(500).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                visibility = View.VISIBLE
            }
        })
    } else {
        visibility = View.VISIBLE
    }
}

fun getFormattedPhone(phone: String): String {
    var phone = phone.replace("-", "")
    phone = phone.replace(" ", "")
    phone = phone.replace("(", "")
    phone = phone.replace(")", "")
    return phone
}

/** Set the View visibility to INVISIBLE and eventually animate view alpha till 0% */
fun View.invisible(animate: Boolean = true) {
    hide(View.INVISIBLE, animate)
}

/** Set the View visibility to GONE and eventually animate view alpha till 0% */
fun View.gone(animate: Boolean = true) {
    hide(View.GONE, animate)
}

/** Convenient method that chooses between View.visible() or View.invisible() methods */
fun View.visibleOrInvisible(show: Boolean, animate: Boolean = true) {
    if (show) visible(animate) else invisible(animate)
}

/** Convenient method that chooses between View.visible() or View.gone() methods */
fun View.visibleOrGone(show: Boolean, animate: Boolean = true) {
    if (show) visible(animate) else gone(animate)
}

fun View.hide(hidingStrategy: Int, animate: Boolean = true) {
    if (animate) {
        animate().alpha(0f).setDuration(500).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = hidingStrategy

            }
        })
    } else {
        visibility = hidingStrategy
    }
}

fun isValidMobile(str: String): Boolean {

    if (str.startsWith("01") && str.length == 11) {
        return true
    } else
        return false

}

fun getSearchInputType(phone: String): Int {
    var str = phone.replace("-", "")
    str = str.replace(" ", "")
    return if (isEmpty(str)) {
        0
    } else {
        if (isValidMobile(str) && ((str.startsWith("+8801") && str.length == 14)
                    || (str.startsWith("8801") && str.length == 13)
                    || (str.startsWith("01") && str.length == 11))
        )
            2
        else
            1

    }
}



fun getAllYears(): MutableList<String?> {
    val data: MutableList<String?> = mutableListOf<String?>()

    try {
        val cal: Calendar = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR)
        for (n in year downTo (year - 3)) {
            data.add(n.toString())
        }

    } catch (e: Exception) {

    }
    return data

}

fun isAtLeastVersion(version: Int): Boolean {
    return Build.VERSION.SDK_INT >= version
}

fun getCurrentDate(): String {
    var Date = ""
    try {
        val cal: Calendar = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        return year.toString() + "-" + "%02d".format(month)
    } catch (e: Exception) {
        Timber.e(e)
    }
    return ""
}

fun getPreviousDate(): String {
    var Date = ""
    try {
        val cal: Calendar = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        return year.toString() + "-" + "%02d".format(month)
    } catch (e: Exception) {
        Timber.e(e)
    }
    return ""
}

fun getCurrentMonth(): Int {
    var month = 0
    try {
        val cal: Calendar = Calendar.getInstance()
        month = cal.get(Calendar.MONTH)
        return month
    } catch (e: Exception) {
        Timber.e(e)
    }
    return month
}

fun getCurrentYear(): Int {
    var year = 0
    try {
        val cal: Calendar = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        return year
    } catch (e: Exception) {
        Timber.e(e)
    }
    return year
}

fun getCurrentDateDisplay(): String {
    try {
        val cal: Calendar = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        return getAllMonth().get(month) + " " + year
    } catch (e: Exception) {
        Timber.e(e)
    }
    return ""
}

fun getPreviousDateDisplay(): String {
    try {
        val cal: Calendar = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        return getAllMonth()[month - 1] + " " + year
    } catch (e: Exception) {
        Timber.e(e)
    }
    return ""
}

fun getAllMonth(): MutableList<String?> {
    val data: MutableList<String?> = mutableListOf<String?>()

    data.add("January")
    data.add("February")
    data.add("March")
    data.add("April")
    data.add("May")
    data.add("June")
    data.add("July")
    data.add("August")
    data.add("September")
    data.add("October")
    data.add("November")
    data.add("December")
    return data

}


fun getCurrentDateReport(): String {
    try {
        val sdf = SimpleDateFormat("dd MMM, yyyy")
        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  " + currentDate)
        return currentDate
    } catch (e: Exception) {
        return ""
    }
}


fun isImageUrl(url: String): Boolean {
    return if (!isEmpty(url) && url.contains("http")) return true else false

}


fun stringToURL(url: String): URL? {
    try {
        var url = URL(url)
        return url
    } catch (e: MalformedURLException) {
        Timber.e(e)
    }
    return null
}


//fun statusMessage(
//    activity: Activity,
//    finalFileName: String,
//    directoryPictures: File,
//    status: Int
//): String {
//    return when (status) {
//        DownloadManager.STATUS_FAILED -> activity.getString(R.string.download_failed_message)
//        DownloadManager.STATUS_PAUSED -> activity.getString(R.string.download_paused_message)
//        DownloadManager.STATUS_PENDING -> activity.getString(R.string.download_pending_message)
//        DownloadManager.STATUS_RUNNING -> activity.getString(R.string.downloading_dots)
//        DownloadManager.STATUS_SUCCESSFUL -> activity.getString(R.string.image_download_successful_message) + directoryPictures + File.separator + finalFileName
//        /*+url.substring(url.lastIndexOf("/") + 1)*/
//        else -> activity.getString(R.string.theres_nothing_to_download)
//    }
//}


fun findMyCountry() {
    var response: String? = ""
    val reqUrl = "https://ipinfo.io/country/"
    Thread {

        try {
            // Create a URL for the desired page
            val url = URL(reqUrl) //My text file location
            //First open the connection
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 60000 // timing out in a minute
            val `in` = BufferedReader(InputStreamReader(conn.inputStream))

            var str: String?
            while (`in`.readLine().also { str = it } != null) {
                Timber.e(" x " + str)
                response += str
            }
            `in`.close()
        } catch (e: Exception) {
            Log.d("MyTag", e.toString())
        }

    }.start()
    Timber.e(" x " + response)

}

//fun playSuccessVoice(mActivity: Activity) {
//    val mPlayer: MediaPlayer
//    mPlayer = MediaPlayer.create(mActivity, R.raw.transaction_success)
//    mPlayer.start()
//}


fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap? {
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    v.draw(c)
    return b
}


fun getPath(context: Activity, uri: Uri): String? {
    val isKitKatorAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKatorAbove && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null

            contentUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        return getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun getValue(value: String): String {
    return if (isEmpty(value)) "----------" else value
}

fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor =
            context.getContentResolver().query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        if (cursor != null) cursor.close()
    }
    return null
}

fun createFinalPdfFilePath(mContext: Activity): String? {
    var finalPdfFile = createPDFFile(mContext)
    return finalPdfFile.absolutePath
}


@Throws(IOException::class)
fun createPDFFile(context: Context): File {
    val foldername = "upay"

    // Create an image file name

    val imageFileName = foldername + createPDFFileName()
    val storageDir: File?
    storageDir = if (context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) != null) {
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    } else {
        context.filesDir
    }
    storageDir!!.mkdirs()

    return File.createTempFile(
        imageFileName,   /* prefix */
        ".pdf",          /* suffix */
        storageDir      /* directory */
    )
}

@Throws(IOException::class)
fun createImageFile(context: Context): File {
    val foldername = "upay"

    // Create an image file name

    val imageFileName = foldername + createFileImageName()
    val storageDir: File?
    storageDir = if (context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) != null) {
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    } else {
        context.filesDir
    }
    storageDir!!.mkdirs()

    return File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",  /* suffix */
        storageDir /* directory */
    )

}

fun isExternalStorageReadOnly(): Boolean {
    val extStorageState = Environment.getExternalStorageState()
    return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
        true
    } else false
}

fun isExternalStorageAvailable(): Boolean {
    val extStorageState = Environment.getExternalStorageState()
    return if (Environment.MEDIA_MOUNTED == extStorageState) {
        true
    } else false
}


fun createPDFFileName(): String {
    val timeStamp: String =
        SimpleDateFormat("ddMMyy_HHmm").format(Date())
    return "_" + timeStamp + ""
}

fun createFileImageName(): String {
    val timeStamp: String =
        SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    return "image_" + timeStamp + ""
}


//fun showImageUploadDialog(
//    mContext: Context,
//    dialogCallback: DialogPhotoUploadListener?
//) {
//    val infoDialog = CustomDialog(mContext, R.style.CustomDialogTheme)
//    val inflator = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    val v: View = inflator.inflate(R.layout.layout_image_chooser, null)
//    infoDialog.setContentView(v)
//    infoDialog.setCancelable(true)
//    infoDialog.getWindow()?.setLayout(
//        ViewGroup.LayoutParams.MATCH_PARENT,
//        ViewGroup.LayoutParams.MATCH_PARENT
//    )
//    val tv_take_camera = infoDialog.findViewById(R.id.tv_take_camera) as TextView
//    val btn_close = infoDialog.findViewById(R.id.btn_close) as AppCompatImageView
//    val tv_select_photo =
//        infoDialog.findViewById(R.id.tv_select_photo) as TextView
//    btn_close.setOnClickListener {
//        infoDialog.dismiss()
//    }
//    tv_take_camera.setOnClickListener {
//        dialogCallback?.onTakePhotoClick()
//        infoDialog.dismiss()
//    }
//
//    tv_select_photo.setOnClickListener {
//        dialogCallback?.onSelectPhotoClick()
//        infoDialog.dismiss()
//    }
//    infoDialog.show()
//}


fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(
        inContext.contentResolver,
        inImage,
        "beats_profile",
        null
    )
    Timber.e("uri->" + path)
    return Uri.parse(path)
}

fun getRealPathFromURI(uri: Uri, activity: Activity): String? {
    val projection =
        arrayOf(MediaStore.Images.Media.DATA)
    val cursor = activity.managedQuery(uri, projection, null, null, null)
    val column_index = cursor
        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    return cursor.getString(column_index)
}

fun getPathFromInputStreamUri(context: Activity, uri: Uri): String? {
    var filePath: String? = null
    uri.authority?.let {
        try {
            context.contentResolver.openInputStream(uri).use {
                val photoFile: File? = createTemporalFileFrom(context, it)
                filePath = photoFile?.path
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return filePath
}

fun getRightAngleImage(photoPath: String?): String? {
    try {
        val ei = ExifInterface(photoPath!!)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        var degree = 0
        degree = when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> 0
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            ExifInterface.ORIENTATION_UNDEFINED -> 0
            else -> 90
        }
        return rotateImage(degree, photoPath!!)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return photoPath
}

fun rotateImage(degree: Int, imagePath: String): String? {
    if (degree <= 0) {
        return imagePath
    }
    try {
        var b = BitmapFactory.decodeFile(imagePath)
        val matrix = Matrix()
        if (b.width > b.height) {
            matrix.setRotate(degree.toFloat())
            b = Bitmap.createBitmap(
                b, 0, 0, b.width, b.height,
                matrix, true
            )
        }
        val fOut = FileOutputStream(imagePath)
        val imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1)
        val imageType = imageName.substring(imageName.lastIndexOf(".") + 1)
        val out = FileOutputStream(imagePath)
        if (imageType.equals("png", ignoreCase = true)) {
            b.compress(Bitmap.CompressFormat.PNG, 100, out)
        } else if (imageType.equals("jpeg", ignoreCase = true) || imageType.equals(
                "jpg",
                ignoreCase = true
            )
        ) {
            b.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        fOut.flush()
        fOut.close()
        b.recycle()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return imagePath
}

@Throws(IOException::class)
private fun createTemporalFileFrom(mContext: Activity, inputStream: InputStream?): File? {
    var targetFile: File? = null

    return if (inputStream == null) targetFile
    else {
        var read: Int
        val buffer = ByteArray(8 * 1024)

        targetFile = createImageFile(mContext)
        FileOutputStream(targetFile).use { out ->
            while (inputStream.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
            out.flush()
        }
        targetFile
    }
}


fun isTopOffer(packType: String): Boolean {
    if (packType != null && !isEmpty(packType) && packType.toLowerCase().equals("exclusive")) {
        return true
    } else return false
}

fun getFormattedDate(dateString: String, currentFormat: String, neededFormat: String): String {
    try {
        val desiredFormat =
            SimpleDateFormat(neededFormat, Locale.ENGLISH)
        val sdf = SimpleDateFormat(currentFormat)


        val date = sdf.parse(dateString)

        return desiredFormat.format(date)
    } catch (e: Exception) {
        return ""
    }
}

var lastTimeClicked: Long = 0


class SafeClickListener(
    private var defaultInterval: Int = 500,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun TextView.setHTMLText(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
    } else {
        this.text = Html.fromHtml(text);
    }
}


fun getBitmapFromURL(src: String?): Bitmap? {
    return try {
        val url = URL(src)
        val connection =
            url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        // Log exception
        null!!
    }
}

@Throws(FileNotFoundException::class, IOException::class)
fun getBitmap(cr: ContentResolver, url: Uri?): Bitmap {
    val input = cr.openInputStream(url!!)
    val bitmap = BitmapFactory.decodeStream(input)
    input!!.close()
    return bitmap
}


fun getPdfViewerUrl(url: String): String {
    return "http://docs.google.com/gview?embedded=true&url=$url"
}


//fun showBadge(
//    context: Context?,
//    bottomNavigationView: BottomNavigationView,
//    @IdRes itemId: Int,
//    value: String?
//) {
//    removeBadge(bottomNavigationView, itemId)
//    val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
//    val badge: View = LayoutInflater.from(context)
//        .inflate(R.layout.badge_layout, bottomNavigationView, false)
//    val text: TextView = badge.findViewById(R.id.badge_text_view)
//    text.text = value
//    itemView.addView(badge)
//}

fun removeBadge(bottomNavigationView: BottomNavigationView, @IdRes itemId: Int) {
    val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
    if (itemView.childCount == 3) {
        itemView.removeViewAt(2)
    }
}

fun isLatestVersion(appVersion: String, systemVersion: String): Boolean {
    var appVersion = appVersion.replace(".", "")
    var index = appVersion.indexOf("-")
    if (index > 0) {
        appVersion = appVersion.substring(0, index)
    }
    var systemVersion = systemVersion.replace(".", "")
    index = systemVersion.indexOf("-")
    if (index > 0) {
        systemVersion = systemVersion.substring(0, index)
    }
    try {
        return getInt(appVersion) >= getInt(systemVersion)
    } catch (e: Exception) {
        return false
    }

}



@SuppressLint("SimpleDateFormat")
fun Calendar.getMonthInFormat(format: String): String {
    val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
    return "${dateFormat.format(this.time)}"
}

@SuppressLint("SimpleDateFormat")
fun String.getMonthInFormat(actualFormatInString: String, formatInString: String): String {
    val actualFormat = SimpleDateFormat(actualFormatInString, Locale.ENGLISH)
    val acutalDate = actualFormat.parse(this)
    val dateFormat = SimpleDateFormat(formatInString, Locale.ENGLISH)
    val formattedDate = dateFormat.format(acutalDate)
    return "${formattedDate}"
}

@SuppressLint("SimpleDateFormat")
fun Calendar.getFirstDayInIntFormat(month: Int, year: Int): Int {
    this.apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.MONTH, month - 1)
        set(Calendar.YEAR, year)
    }
    val dateFormat = SimpleDateFormat("dd", Locale.ENGLISH)
    return "${dateFormat.format(this.time)}".toInt()
}

fun Calendar.getLastDayInIntFormat(month: Int, year: Int): Int {
    this.apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.MONTH, month - 1)
        set(Calendar.YEAR, year)
        add(Calendar.MONTH, 1)
        set(Calendar.DAY_OF_MONTH, 1)
        add(Calendar.DATE, -1)
    }
    val dateFormat = SimpleDateFormat("dd", Locale.ENGLISH)
    return "${dateFormat.format(this.time)}".toInt()
}


@SuppressLint("SimpleDateFormat")
fun String.hashTagEventFormattedDate(): Int {
    val actualFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
    val actualDate = actualFormat.parse(this)
    val requiredFormat = SimpleDateFormat("dd", Locale.ENGLISH)
    val requiredDate = requiredFormat.format(actualDate)
    return requiredDate.toInt()
}

fun getEmotionSmallList(): List<Boolean?> {
    val data: MutableList<Boolean?> = mutableListOf<Boolean?>()
    data.add(true)
    data.add(true)
    for (item in 0..10) {
        data.add(false)
    }
    data.add(true)

    return data
}


fun getRoundedBG(radius: Float): Drawable {
    val shape = GradientDrawable()
    shape.shape = GradientDrawable.RECTANGLE
    shape.setColor(Color.WHITE)
    //  shape.setStroke(3, Color.YELLOW)
    shape.cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
    return shape
}



fun Context.getMyDrawable(id: Int): Drawable? {

    return ContextCompat.getDrawable(this, id)
}


fun getDistanceByZoomLevel(zoom: Double): Double {
    var value = 22 - zoom

    var result = Math.pow(2.toDouble(), value.toDouble())
    result = result * 5
    Log.e("distance by Zoom", result.toString())
    return result
}

fun min(x: Int, y: Int): Int {
    return if (x < y) x else y
}

fun max(x: Int, y: Int): Int {
    return if (x > y) x else y
}

fun getCurrentDay(): Int {
    var day = 0
    try {
        val df = SimpleDateFormat("dd")
        val date = df.format(Calendar.getInstance().time)
        day = date.toInt()
    } catch (e: Exception) {
    }
    return -day
}

fun rotateImage(
    img: Bitmap?,
    degree: Int
): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImg: Bitmap = Bitmap.createBitmap(img!!, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
}

@Throws(IOException::class)
fun rotateImageIfRequired(
    context: Context,
    img: Bitmap?,
    selectedImage: Uri
): Bitmap? {
    val input =
        context.contentResolver.openInputStream(selectedImage)
    val ei: ExifInterface
    ei =
        if (Build.VERSION.SDK_INT > 23) ExifInterface(input!!) else ExifInterface(selectedImage!!.path!!)
    val orientation = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
            img,
            90
        )
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
            img,
            180
        )
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
            img,
            270
        )
        else -> img
    }
}

inline fun safeExecutor(crossinline block: () -> Unit) {
    try {
        block.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



