package com.example.bs_test.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import com.example.bs_test.util.EMPTY

import com.example.bs_test.utils.sharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var CUSTOMER: Boolean
    var loginCompleted: Boolean
    var platform: Boolean
    var token: String
    var refreshToken: String
    var mobileNumber: String
    var pin: String
    var language: String
    var customerName: String
    var image: String
    var spotifyToken: String
    var updateToken: String
    var AppVersionName: Int
    var user_id: String
    var first_time_post: Boolean
}

class SharedPreferenceStorage(context: Context) : PreferenceStorage {


    private val prefs: Lazy<SharedPreferences> = lazy {
        context.sharedPreferences(PREFS_NAME)
    }
    override var CUSTOMER by BooleanPreference(prefs, PREF_CUSTOMER)
    override var loginCompleted by BooleanPreference(prefs, PREF_LOGIN)
    override var platform by BooleanPreference(prefs, PREF_PLATFORM)
    override var token by StringPreference(prefs, PREF_TOKEN, "")
    override var updateToken by StringPreference(prefs, PREF_UPDATE_TOKEN, "")
    override var refreshToken by StringPreference(prefs, PREF_REFRESH_TOKEN, "")
    override var spotifyToken by StringPreference(prefs, PREF_SPROTIFY_TOTEN, "")
    override var mobileNumber by StringPreference(prefs, PREF_MOBILE_NUMBER, "")
    override var pin by StringPreference(prefs, PREF_PIN, "")
    override var user_id by StringPreference(prefs, PREF_USER_ID, String.EMPTY)
    override var first_time_post: Boolean by BooleanPreference(prefs, FIRST_POST, true)

    // override var contact by StringPreference(prefs, PREF_CONNECTION_TYPE, "")
    override var language by StringPreference(prefs, LANGUAGE, "")
    override var customerName by StringPreference(prefs, CUSTOMER_NAME, "")

    override var image by StringPreference(prefs, IMAGE, "")

    override var AppVersionName by IntPreference(prefs, APP_VERSION_NAME, 0)


    //  override var customerInfo by CustomerInfoPreference(prefs, PREF_CUSTOMER_INFO, "")


    companion object {
        const val PREFS_NAME = "customer"
        const val CUSTOMER_NAME = "customer_name"

        const val PREF_CUSTOMER = "pref_customer"
        const val PREF_LOGIN = "pref_login"
        const val PREF_PLATFORM = "pref_platform"
        const val PREF_TOKEN = "pref_token"
        const val PREF_UPDATE_TOKEN = "pref_update_token"
        const val PREF_REFRESH_TOKEN = "pref_refreshToken"
        const val PREF_MOBILE_NUMBER = "pref_mobileNumber"
        const val PREF_SPROTIFY_TOTEN = "pref_spotify_token"
        const val PREF_PIN = "pref_pin"
        const val LANGUAGE = "lnaguage"
        const val IMAGE = "image"
        const val PREF_USER_ID = "pref_pin"
        const val APP_VERSION_NAME = "app_version_name"
        const val FIRST_POST = "first_post"
    }
}

class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean = false
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}


class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return preferences.value.getString(name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}

class StringPreferenceFloat(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Float
) : ReadWriteProperty<Any, Float?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return preferences.value.getFloat(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float?) {
        preferences.value.edit { putFloat(name, value!!) }
    }
}

class IntPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {
    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.value.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.value.edit { putInt(name, value) }
    }
}


class LongPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Long
) : ReadWriteProperty<Any, Long> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return preferences.value.getLong(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        preferences.value.edit { putLong(name, value) }
    }
}


//
//class AccountListPreference(
//    private val preferences: Lazy<SharedPreferences>,
//    private val name: String,
//    private val defaultValue: String
//) : ReadWriteProperty<Any, MutableList<TaggedCasa>?> {
//    @WorkerThread
//    override fun getValue(thisRef: Any, property: KProperty<*>): MutableList<TaggedCasa>? {
//        val moshe = Moshi.Builder().build()
//        val value = preferences.value.getString(name, defaultValue) ?: defaultValue
//        val listOfCardsType: Type = Types.newParameterizedType(MutableList::class.java, TaggedCasa::class.java)
//        val jsonAdapter: JsonAdapter<MutableList<TaggedCasa>> = moshe.adapter(listOfCardsType)
//        return try {
//            val list = jsonAdapter.fromJson(value)
//            list
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    override fun setValue(thisRef: Any, property: KProperty<*>, value: MutableList<TaggedCasa>?) {
//        val moshe = Moshi.Builder().build()
//        val listOfCardsType: Type = Types.newParameterizedType(MutableList::class.java, TaggedCasa::class.java)
//        val jsonAdapter: JsonAdapter<MutableList<TaggedCasa>> =
//            moshe.adapter(listOfCardsType)
//        val json = jsonAdapter.toJson(value)
//        preferences.value.edit { putString(name, json) }
//    }
//}


