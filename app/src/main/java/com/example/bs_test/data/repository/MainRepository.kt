package com.example.bs_test.data.repository

import com.example.bs_test.data.model.*
import com.example.bs_test.data.network.Resource
import com.example.bs_test.data.network.api.ApiHelper
import com.example.bs_test.data.network.api.ApiService
import com.example.bs_test.data.network.api.SafeApiRequest
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(val apiService: ApiService,
                                         private val apiHelper: ApiHelper,): SafeApiRequest() {


    suspend fun getGitRepositories(q: String?,sort: String?,order: String?,perPage: Int?,page: Int?): Resource<GetRepos> {
        return apiResponse { apiService.getGitRepositories(q, sort,order,perPage ,page) }
    }



}