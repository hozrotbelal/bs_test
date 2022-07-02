package com.example.bs_test.data.network.api


import android.util.Log
import com.example.bs_test.data.network.Resource
import com.example.bs_test.utils.ApiException
import com.example.bs_test.utils.NoInternetException


import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber

open class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                val error = response.errorBody()?.string()
                val message = StringBuilder()
                error?.let {
                    try {
                        message.append(JSONObject(it).getString("message"))
                    } catch (e: JSONException) {
                    }
                }
                Log.e("Error code:", " ${response.code()}")
                throw  ApiException(message = message.toString())
            }
        } catch (e: NoInternetException) {
            println(e.localizedMessage)
            throw NoInternetException("Network Error")
        } catch (e: Exception) {
            Timber.e(e.message)
            throw  ApiException(message = e.localizedMessage)
        }
    }

    suspend fun <T : Any> apiResponse(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                return Resource.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string()
                val message = StringBuilder()
                error?.let {
                    try {
                        message.append(JSONObject(it).getString("message"))
                    } catch (e: JSONException) {
                        return Resource.error(ApiError("", message.toString(), ""))
                    }
                }
                Log.e("Error code:", " ${response.code()}")
                return Resource.error(ApiError("", message.toString(), ""))
            }
        } catch (e: NoInternetException) {
            println(e.localizedMessage)
            return Resource.error(ApiError("","Network Error", ""))
        } catch (e: Exception) {
            Timber.e(e.message)
            return Resource.error(ApiError("",e.localizedMessage, ""))
        }
    }

}