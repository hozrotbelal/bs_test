package com.example.bs_test.data.network.api

import androidx.lifecycle.LiveData
import com.example.bs_test.data.model.*
import com.example.bs_test.data.network.Resource
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Path


interface ApiHelper {
   //override suspend fun getContributors(ownerName: String?,repoName: String?,): Resource<List<Owner>> = apiService.getContributors(ownerName, repoName)
   suspend fun getContributors(ownerName: String?,repoName: String?,): Response<List<Owner>>
}