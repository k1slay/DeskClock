package com.k2.deskclock.commons.di

import android.content.Context
import com.k2.deskclock.commons.preferences.PreferenceKeys.PREF_FILE
import com.k2.deskclock.commons.preferences.PreferenceStore
import com.k2.deskclock.commons.preferences.PreferenceStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonsModule {
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Provides
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providesOkHttp(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
    }

    @Provides
    fun providesPreferenceStore(
        @ApplicationContext context: Context,
    ): PreferenceStore {
        return PreferenceStoreImpl(context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE))
    }

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

}
