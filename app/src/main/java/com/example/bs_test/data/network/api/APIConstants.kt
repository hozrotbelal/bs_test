package com.example.bs_test.data.network.api

class APIConstants {

    companion object {
        const val BASE_URL : String = "https://api.github.com"

        const val GET_REPOS : String =  BASE_URL+"/search/repositories"
        const val GET_CONTRIBUTORS = "/repos/{ownerName}/{repoName}/contributors"

    }

}