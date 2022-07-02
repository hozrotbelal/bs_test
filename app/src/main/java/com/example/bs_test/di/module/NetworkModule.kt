package com.example.bs_test.di.module

import android.content.Context
import com.example.bs_test.BuildConfig
import com.example.bs_test.data.network.api.ApiService

import com.example.bs_test.data.network.interceptor.CoilInterceptor
import com.example.bs_test.data.storage.PreferenceStorage
import coil.util.CoilUtils
import com.example.bs_test.data.network.adapter.LiveDataCallAdapterFactory
import com.example.bs_test.data.network.api.APIConstants
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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
//        return API_URL
//    }
    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun provideCoilInterceptor(
        storage: PreferenceStorage
    ): CoilInterceptor {
        return CoilInterceptor(storage)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(coilInterceptor: CoilInterceptor): OkHttpClient {
        val mBuilder = OkHttpClient.Builder()
            .readTimeout(300, TimeUnit.SECONDS)
            .callTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) mBuilder.addNetworkInterceptor(StethoInterceptor())
        mBuilder.addInterceptor(coilInterceptor)

        return mBuilder.build()
    }


//    @Provides
//    @Singleton
//    fun provideOkHttpClient(storage: PreferenceStorage) = if (BuildConfig.DEBUG) {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .readTimeout(60, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(false)
//            .addNetworkInterceptor(StethoInterceptor())
//            .  addInterceptor(CoilInterceptor(storage))
//            .build()
//
//    } else OkHttpClient
//        .Builder()
//        .build()

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
    fun providesRetrofit(mClient: OkHttpClient, mMoshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .client(mClient)
            .baseUrl(APIConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(mMoshi))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providesbeatsApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


}