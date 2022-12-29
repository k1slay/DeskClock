package com.k2.deskclock.wallpaper.data.local

import com.k2.deskclock.wallpaper.data.models.WallpaperCacheDao

interface WallpaperDaoProvider {
    fun wallpaperCacheDao(): WallpaperCacheDao
}
