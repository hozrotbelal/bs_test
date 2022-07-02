package com.example.bs_test.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommonResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "success")
    val success: Boolean?,

)

