package com.example.bs_test.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Hozrot Belal
 */

@Parcelize
data class GetRepos(
    @Json(name = "total_count")
    val totalCount: Int?,

    @Json(name = "incomplete_results")
    val incompleteResults: Boolean?,

    @Json(name ="items")
    val items: MutableList<Item>? = null
): Parcelable {
}