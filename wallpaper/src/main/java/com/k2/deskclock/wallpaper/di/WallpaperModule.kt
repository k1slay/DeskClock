package com.k2.deskclock.wallpaper.di

import com.k2.deskclock.commons.preferences.PreferenceStore
import com.k2.deskclock.wallpaper.BuildConfig
import com.k2.deskclock.wallpaper.WallpaperProvider
import com.k2.deskclock.wallpaper.WallpaperProviderImpl
import com.k2.deskclock.wallpaper.data.Constants.API_ENDPOINT
import com.k2.deskclock.wallpaper.data.Constants.CLIENT_ID
import com.k2.deskclock.wallpaper.data.local.WallpaperDaoProvider
import com.k2.deskclock.wallpaper.data.local.WallpaperLocalDataSource
import com.k2.deskclock.wallpaper.data.local.WallpaperLocalDataSourceImpl
import com.k2.deskclock.wallpaper.data.models.WallpaperCacheDao
import com.k2.deskclock.wallpaper.data.remote.WallpaperApiInterface
import com.k2.deskclock.wallpaper.data.remote.WallpaperRemoteDataSource
import com.k2.deskclock.wallpaper.data.remote.WallpaperRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object WallpaperModule {

    @Provides
    fun provideRemoteDataSource(
        weatherApi: WallpaperApiInterface,
        @Named(CLIENT_ID) clientId: String,
        preferenceStore: PreferenceStore
    ): WallpaperRemoteDataSource {
        return WallpaperRemoteDataSourceImpl(weatherApi, clientId, preferenceStore)
    }

    @Provides
    fun provideLocalWallpaperStore(
        dao: WallpaperCacheDao,
        preferenceStore: PreferenceStore
    ): WallpaperLocalDataSource {
        return WallpaperLocalDataSourceImpl(dao, preferenceStore)
    }

    @Provides
    fun provideWallpaperDao(wallpaperDaoProvider: WallpaperDaoProvider): WallpaperCacheDao {
        return wallpaperDaoProvider.wallpaperCacheDao()
    }

    @Provides
    fun provideWeatherApi(@WallpaperRetrofit retrofit: Retrofit): WallpaperApiInterface {
        return retrofit.create(WallpaperApiInterface::class.java)
    }

    @Provides
    @WallpaperRetrofit
    fun provideRetrofit(
        @Named(API_ENDPOINT) endPoint: String,
        okHttpClient: OkHttpClient.Builder,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl(endPoint)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Named(CLIENT_ID)
    fun provideClientId(): String {
        return BuildConfig.UNSPLASH_CLIENT_ID
    }

    @Provides
    @Named(API_ENDPOINT)
    fun provideApiEndpoint(): String {
        return "https://api.unsplash.com/"
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class BoundWallpaperModule {
    @Binds
    abstract fun bindWallpaperProvider(
        wallpaperProvider: WallpaperProviderImpl
    ): WallpaperProvider
}
