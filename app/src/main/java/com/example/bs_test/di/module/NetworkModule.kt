package com.example.bs_test.di.module

import android.content.Context
import coil.util.CoilUtils
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.bs_test.BuildConfig
import com.example.bs_test.data.network.adapter.LiveDataCallAdapterFactory
import com.example.bs_test.data.network.api.APIConstants
import com.example.bs_test.data.network.api.ApiHelper
import com.example.bs_test.data.network.api.ApiHelperImpl
import com.example.bs_test.data.network.api.ApiService
import com.example.bs_test.data.network.interceptor.CoilInterceptor
import com.example.bs_test.data.storage.PreferenceStorage

import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton





@Qualifier
annotation class CoilCache

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {


//    @Singleton
//    @Provides
//    fun provideBaseUrl(): HttpUrl {
//        return BuildConfig.BASE_DOMAIN_URL.toHttpUrl()
//    }
    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(storage: PreferenceStorage) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addNetworkInterceptor(StethoInterceptor())
            .  addInterceptor(CoilInterceptor(storage))
            .build()

    } else OkHttpClient
        .Builder()
        .build()

//    @Provides
//    @Singleton
//    @DefaultCache
//    fun getCacheIterator(@ApplicationContext ctx: Context): Cache{
//        val cacheSize = 25 * 1024 * 1024 // 25 MB
//        return Cache(ctx.cacheDir, cacheSize.toLong())
//    }

    @Provides
    @Singleton
    @CoilCache
    fun getCoilCache(@ApplicationContext ctx: Context): Cache {
        return CoilUtils.createDefaultCache(ctx)
    }

    @Provides
    @Singleton
    fun providesRetrofit( mClient: OkHttpClient, mMoshi: Moshi): Retrofit {

        return Retrofit.Builder()
            .client(mClient)
            .baseUrl(APIConstants.BASE_URL)
         // .addConverterFactory(GsonConverterFactory.create())
           .addConverterFactory(MoshiConverterFactory.create(mMoshi))
      // .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }
    @Provides
    @Singleton
    fun providesbeatsApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }





    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper


}