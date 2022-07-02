package com.example.bs_test.data.model

import com.example.bs_test.exception.Error


sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val error: Error) : Resource<T>()
}