package com.k2.deskclock.localstore.di

import android.content.Context
import androidx.room.Room
import com.k2.deskclock.localstore.DeskClockDb
import com.k2.deskclock.wallpaper.data.local.WallpaperDaoProvider
import com.k2.deskclock.weather.data.local.WeatherDaoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStoreModule {

    @Provides
    @Singleton
    fun provideAppDb(@ApplicationContext context: Context): DeskClockDb {
        return Room.databaseBuilder(
            context.applicationContext,
            DeskClockDb::class.java,
            "DeskClockDb.db"
        ).build()
    }

    @Provides
    fun provideWeatherDaoProvider(deskClockDb: DeskClockDb): WeatherDaoProvider {
        return deskClockDb
    }

    @Provides
    fun provideWallpaperDaoProvider(deskClockDb: DeskClockDb): WallpaperDaoProvider {
        return deskClockDb
    }

}
