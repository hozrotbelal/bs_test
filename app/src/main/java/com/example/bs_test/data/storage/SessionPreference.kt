package com.example.bs_test.data.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData


class SessionPreference(private val pref: SharedPreferences, private val context: Context) {
    

    val customerNameLiveData = MutableLiveData<String>()

    
    var phoneNumber: String
        get() = pref.getString(PREF_PHONE_NUMBER, "") ?: ""
        set(phoneNumber) = pref.edit { putString(PREF_PHONE_NUMBER, phoneNumber) }
    

    
    var customerName: String
        get() = pref.getString(PREF_CUSTOMER_NAME, "") ?: ""
        set(customerName) {
            customerNameLiveData.postValue(customerName)
            pref.edit { putString(PREF_CUSTOMER_NAME, customerName) }
        }
    



    companion object {
        private const val PREF_PHONE_NUMBER = "p_number"
        private const val PREF_CUSTOMER_NAME = "customer_name"

        
        @SuppressLint("StaticFieldLeak")
        private var instance: SessionPreference? = null

        fun init(mContext: Context) {
            if (instance == null) {
                instance = SessionPreference(mContext.getSharedPreferences("Beats", Context.MODE_PRIVATE), mContext)
            }
        }

        fun getInstance(): SessionPreference {
            if (instance == null) {
                throw InstantiationException("Instance is null...call init() first")
            }
            return instance as SessionPreference
        }
    }
}
