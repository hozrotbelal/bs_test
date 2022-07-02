package com.example.bs_test.data.storage

import android.content.Context
var HAS_NEW_NOTIFICATION = "notification_state"
const val MyPREFERENCES = "thebeats"


fun saveData(
    context: Context?,
    key: String?,
    value: String?

) {
    if (context != null) {
        val prefs = context.getSharedPreferences(MyPREFERENCES, 0)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
}
fun saveBoolData(
    context: Context?,
    key: String?,
    value: Boolean?

) {
    if (context != null) {
        val prefs = context.getSharedPreferences(MyPREFERENCES, 0)
        val editor = prefs.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }
}
fun getData( context: Context?,key: String?): String{
    var string = ""
    if (context != null) {
        val prefs = context.getSharedPreferences(MyPREFERENCES, 0)
        string = prefs.getString(key, "")!!
    }
    return string
}
fun getBoolData(context: Context?,key: String? ): Boolean {
    var string = false
    if (context != null) {
        val prefs = context.getSharedPreferences(MyPREFERENCES, 0)
        string = prefs.getBoolean(key, false)
    }
    return string
}