package com.example.bs_test.data.network

import com.example.bs_test.data.network.api.ApiError

data class Resource<out T>(val status: Status, val data: T?, val error: ApiError?) {
    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCESS, data, null)
        fun <T> error(apiError: ApiError, data: T?): Resource<T> = Resource(Status.ERROR, data, apiError)

        fun <T> error(apiError: ApiError): Resource<T> = error(apiError, null)
        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data, null)
        fun <T> loading(): Resource<T> = Resource(Status.LOADING, null, null)
    }
}
