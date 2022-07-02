package com.example.bs_test.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id")
    val id: Int ?= 0,
    @Json(name = "name")
    val name: String ?= "",
    @Json(name = "email")
    val email: String? = "",
   @Json(name = "lang")
    val lang: String? = ""


)