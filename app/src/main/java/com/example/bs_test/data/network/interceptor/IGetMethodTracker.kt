package com.example.bs_test.data.network.interceptor

interface IGetMethodTracker {

    fun shouldConvertToGetRequest(urlEncodedFragmentString: String):Boolean
}