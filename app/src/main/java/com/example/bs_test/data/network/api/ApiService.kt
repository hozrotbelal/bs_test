package com.example.bs_test.data.network.api

import com.example.bs_test.data.model.GetRepos
import com.example.bs_test.data.model.Owner
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET(APIConstants.GET_REPOS)
    suspend fun getGitRepositories(
        @Query("q") q: String?,
        @Query("sort") sort: String?,
        @Query("order") order: String?,
        @Query("per_page") perPage: Int?,
        @Query("page") page: Int?
    ): Response<GetRepos>

    @GET(APIConstants.GET_CONTRIBUTORS)
    suspend fun getContributors(
        @Path("ownerName") ownerName: String?,
        @Path("repoName") repoName: String?,
    ): Response<List<Owner>>

}