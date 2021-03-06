package com.example.bs_test.extension

import android.content.res.Resources
import android.util.Patterns
import android.view.View

import com.example.bs_test.enums.InputType
import com.example.bs_test.enums.InputType.*

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

private const val TITLE_PATTERN = "^[\\w\\d_.-]+$"
//private const val EMAIL_PATTERN = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-z]{2,4}$"
private const val ADDRESS_PATTERN = ""
private const val DESCRIPTION_PATTERN = ""
private const val PHONE_PATTERN = "^(?:\\+8801|01)(?:\\d{9})$"

fun String.isValid(type: InputType): Boolean{
    return when(type){
        TITLE -> this.isNotBlank() and TITLE_PATTERN.toRegex().matches(this)
        EMAIL -> this.isNotBlank() and Patterns.EMAIL_ADDRESS.toRegex().matches(this)
        ADDRESS -> this.isNotBlank() and ADDRESS_PATTERN.toRegex().matches(this)
        DESCRIPTION -> this.isNotBlank() and DESCRIPTION_PATTERN.toRegex().matches(this)
        PHONE -> this.isNotBlank() and PHONE_PATTERN.toRegex().matches(this)
    }
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

val Int.dp: Int get() {
    return (this/Resources.getSystem().displayMetrics.density).toInt()
}

val Float.dp: Float get() {
    return (this/Resources.getSystem().displayMetrics.density)
}

val Int.sp: Int get() {
    return (this/Resources.getSystem().displayMetrics.scaledDensity).toInt()
}

val Float.sp: Float get() {
    return (this/Resources.getSystem().displayMetrics.scaledDensity)
}

val Int.px: Int get() {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

val Float.px: Float get() {
    return (this * Resources.getSystem().displayMetrics.density)
}

fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }

fun Long.toFormattedDate(): String{
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"))
    val cal = Calendar.getInstance(TimeZone.getDefault())
    cal.timeInMillis = this
    val dateGMT = cal.time
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    return sdf.format(dateGMT)
}




fun String.toMD5(): String {
    return try {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        bytes.joinToString("") { "%02x".format(it) }
    } catch (e: Exception) {
        ""
    }
}