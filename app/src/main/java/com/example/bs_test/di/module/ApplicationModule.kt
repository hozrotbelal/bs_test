package com.example.bs_test.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.bs_test.data.storage.PreferenceStorage
import com.example.bs_test.data.storage.SessionPreference
import com.example.bs_test.data.storage.SharedPreferenceStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class AppCoroutineScope

@Qualifier
annotation class SessionPreference
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {



    @Provides
    @com.example.bs_test.di.module.SessionPreference
    fun providesSessionSharedPreference(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences("jayson", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesPreference(@com.example.bs_test.di.module.SessionPreference pref: SharedPreferences, @ApplicationContext ctx: Context): SessionPreference {
        return SessionPreference(pref, ctx)
    }
    @Provides
    @Singleton
    fun providesPreferenceStorage(@ApplicationContext context: Context): PreferenceStorage = SharedPreferenceStorage(context)

    @Provides
    @Singleton
    @AppCoroutineScope
    fun providesApplicationCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }


}