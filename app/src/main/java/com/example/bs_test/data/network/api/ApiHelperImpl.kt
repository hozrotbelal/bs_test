package com.example.bs_test.data.network.api

import com.example.bs_test.data.model.GetRepos
import com.example.bs_test.data.model.Owner
import com.example.bs_test.data.network.Resource
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

//    suspend fun getContributors(ownerName: String?,repoName: String?,): Resource<List<Owner>> {
//        return apiResponse { apiService.getContributors(ownerName, repoName) }
//    }

    override suspend fun getContributors(ownerName: String?,repoName: String?,): Response<List<Owner>> = apiService.getContributors(ownerName, repoName)

}