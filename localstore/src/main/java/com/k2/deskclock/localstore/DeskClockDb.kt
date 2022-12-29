package com.k2.deskclock.localstore

import androidx.room.Database
import androidx.room.RoomDatabase
import com.k2.deskclock.wallpaper.data.local.WallpaperDaoProvider
import com.k2.deskclock.wallpaper.data.models.WallpaperCache
import com.k2.deskclock.wallpaper.data.models.WallpaperCacheDao
import com.k2.deskclock.weather.data.local.WeatherDaoProvider
import com.k2.deskclock.weather.data.models.AddressCacheDao
import com.k2.deskclock.weather.data.models.AddressLocalCache
import com.k2.deskclock.weather.data.models.WeatherCacheDao
import com.k2.deskclock.weather.data.models.WeatherLocalCache

@Database(
    entities = [WeatherLocalCache::class, AddressLocalCache::class, WallpaperCache::class],
    version = 1
)
abstract class DeskClockDb : RoomDatabase(), WeatherDaoProvider, WallpaperDaoProvider {
    abstract override fun weatherCacheDao(): WeatherCacheDao
    abstract override fun addressCacheDao(): AddressCacheDao
    abstract override fun wallpaperCacheDao(): WallpaperCacheDao
}
