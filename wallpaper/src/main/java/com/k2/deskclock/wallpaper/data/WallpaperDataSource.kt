package com.k2.deskclock.wallpaper.data

import com.k2.deskclock.wallpaper.data.models.Wallpaper

interface WallpaperDataSource {
    suspend fun getWallpapers(): List<Wallpaper>?
}
