package com.example.bs_test

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.databinding.DataBindingUtil
import coil.Coil
import coil.ImageLoader
import coil.imageLoader
import com.example.bs_test.di.databinding.CustomBindingComponentBuilder
import com.example.bs_test.di.databinding.CustomBindingEntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.android.HiltAndroidApp
import com.example.bs_test.data.network.interceptor.CoilInterceptor
import com.example.bs_test.di.module.CoilCache
import com.example.bs_test.utils.LocaleHelper
import com.example.bs_test.di.module.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Provider
import timber.log.Timber
@HiltAndroidApp
class App : Application() {
    @CoilCache
    lateinit var coilCache: Cache
    @Inject
    @AppCoroutineScope
    lateinit var coroutineScope: CoroutineScope
    @Inject
    lateinit var coilInterceptor: CoilInterceptor

    @Inject lateinit var bindingComponentProvider: Provider<CustomBindingComponentBuilder>
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val dataBindingComponent = bindingComponentProvider.get().build()
        val dataBindingEntryPoint = EntryPoints.get(
            dataBindingComponent, CustomBindingEntryPoint::class.java
        )
        DataBindingUtil.setDefaultComponent(dataBindingEntryPoint)

        initCoil()

    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }


    private fun initCoil() {
        val imageLoader = ImageLoader.Builder(this).apply {
            crossfade(true)
//            availableMemoryPercentage(0.2)
//            bitmapPoolPercentage(0.4)
            okHttpClient {
                OkHttpClient.Builder()
                    .cache(coilCache)
                    .addInterceptor(coilInterceptor)
                    .build()
            }

        }.build()
        Coil.setImageLoader(imageLoader)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        with(imageLoader) {
            bitmapPool.clear()
            memoryCache.clear()
        }
    }

    companion object {
        lateinit var context: Context


    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }
}
