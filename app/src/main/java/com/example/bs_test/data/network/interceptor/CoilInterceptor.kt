package com.example.bs_test.data.network.interceptor


import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.bs_test.App.Companion.context
import com.example.bs_test.data.storage.PreferenceStorage
import com.example.bs_test.util.isEmpty
import com.example.bs_test.utils.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoilInterceptor @Inject constructor(
    val preferences: PreferenceStorage
) : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {


        synchronized(this) {

            // val request = request(chain, preferences.token,preferences.updateToken)
            val request = request(chain)

            val initialResponse = chain.proceed(request)
            val code = initialResponse.code
            Timber.e("code " + code.toString() + "\n" + request.toString())


            when {
                code == 423 -> {
                    return initialResponse
                }
//                code == 401 && preferences.token.isNotEmpty() -> {
//                    logout()
//                    return initialResponse
//                    val tokenResponse = runBlocking {
//                        val build = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//                        val client = OkHttpClient.Builder()
//                            .addNetworkInterceptor(StethoInterceptor()).build()
//                        val retrofit = Retrofit.Builder()
//                            .client(client)
//                            .baseUrl(BuildConfig.BASE_DOMAIN_URL)
//                            .addConverterFactory(MoshiConverterFactory.create(build)).build()
//                        val apiService = retrofit.create(ApiService::class.java)
//                        apiService.getSpotifyToken(GrandType, RefreshToken).execute()
//
//
//                    }

//                    return when {
//                        tokenResponse.code() != 200 -> {
//                            initialResponse
//                        }
//                        else -> {
//
//                            val refreshTokenResponse = tokenResponse.body()
//                            return if (refreshTokenResponse == null) {
//                                initialResponse
//                            } else {
//                                val token = refreshTokenResponse.accessToken
//                                preferences.spotifyToken=token
//                              //  spotifyTokenUseCase(token)
//                                //  refreshTokenUseCase(refreshToken)
//                                initialResponse.close()
//                                val newRequest = request(chain)
//                                chain.proceed(newRequest)
//                            }
//                        }
//                    }
                // }
                else -> {
                    Timber.e(initialResponse.toString())
                    Timber.e(initialResponse.body.toString())
                    if (code == 401) {
                        logout()
                    }
                    return initialResponse
                }


            }

        }


    }

//    private fun deleteCacheEntry(urlToDelete: HttpUrl): Boolean {
//        // report non-fatal only after deletion, so logs include result
//        return evictCorruptedCacheEntry(urlToDelete)
//    }

//    private fun evictCorruptedCacheEntry(httpUrl: HttpUrl): Boolean {
//        val urlToDelete = httpUrl.toString()
//        val iterator = cache.urls()
//        var removed = false
//        while (iterator.hasNext()) {
//            val url = iterator.next()
//            if (url == urlToDelete) {
//                iterator.remove()
//                removed = true
//            }
//        }
//
//        if (!removed) {
//            cache.evictAll()
//        }
//
//        return removed
//    }

    private fun request(chain: Interceptor.Chain): Request {
        val original = chain.request()
        val originalHttpUrl = original.url
        Timber.e("request called" + original.url.toString())
        try {

//var token=if(isEmpty(token)) updateToken else token
            val requestBuilder = original.newBuilder()
//                if(!isEmpty(token))
//                    requestBuilder.addHeader("Authorization", "Bearer "+if(isEmpty(token)) updateToken else token)
                .url(originalHttpUrl)
            return requestBuilder.build()

        } catch (e: Exception) {

            Timber.e(e.message)
            val requestBuilder = original.newBuilder()
                // .addHeader("Authorization", token.JWT)
                //  .addHeader("Authorization", "Basic YjcxYThkMmJjMDI3NGE3MGEwY2NkZDExZjIyZjcwNjI6MTI3YzI5MDgxMmIzNDRhMWI4ZDUxNDU1ZGRmMjc1YTA=")
                // .addHeader("Accept-Language", storage.language)
                .url(originalHttpUrl)
            return requestBuilder.build()
        }
    }

    private fun logout() {
        preferences.token = ""
        preferences.updateToken = ""
        Timber.e("Logout-")
        val intent = Intent(ACTION_LOGOUT)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
