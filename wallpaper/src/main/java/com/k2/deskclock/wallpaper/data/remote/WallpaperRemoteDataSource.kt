package com.k2.deskclock.wallpaper.data.remote

import com.k2.deskclock.commons.preferences.PreferenceStore
import com.k2.deskclock.wallpaper.data.WallpaperDataSource
import com.k2.deskclock.wallpaper.data.models.UnsplashResponse
import com.k2.deskclock.wallpaper.data.models.Wallpaper
import com.k2.deskclock.wallpaper.data.models.toWallpapers

interface WallpaperRemoteDataSource : WallpaperDataSource

class WallpaperRemoteDataSourceImpl(
    private val wallpaperApi: WallpaperApiInterface,
    private val clientId: String,
    private val preferenceStore: PreferenceStore,
) : WallpaperRemoteDataSource {
    override suspend fun getWallpapers(): List<Wallpaper>? {
        val category = preferenceStore.wallpaperCategory
        val response: List<UnsplashResponse>? =
            kotlin.runCatching {
                wallpaperApi.fetchWallpaper(clientId, category).body()
            }.getOrNull()
        return response?.toWallpapers()
    }

}
