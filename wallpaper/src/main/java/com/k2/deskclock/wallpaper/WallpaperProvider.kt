@file:OptIn(DelicateCoroutinesApi::class)

package com.k2.deskclock.wallpaper

import android.util.Log
import com.k2.deskclock.commons.di.IoDispatcher
import com.k2.deskclock.wallpaper.data.local.WallpaperLocalDataSource
import com.k2.deskclock.wallpaper.data.models.Wallpaper
import com.k2.deskclock.wallpaper.data.remote.WallpaperRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

interface WallpaperProvider {
    suspend fun fetchNextWallpaper(): Wallpaper?
}

class WallpaperProviderImpl @Inject internal constructor(
    private val remoteDataSource: WallpaperRemoteDataSource,
    private val localDataSource: WallpaperLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WallpaperProvider {

    @Suppress("DeferredResultUnused")
    override suspend fun fetchNextWallpaper(): Wallpaper? {
        val hasUnseenWallpapers = localDataSource.hasUnseenWallpapers()
        if (hasUnseenWallpapers) {
            return getLocalWallpaper()
        } else {
            remoteDataSource.getWallpapers()?.let {
                val wp = it[0]
                GlobalScope.launch(ioDispatcher) {
                    localDataSource.cacheWallpaper(it)
                    localDataSource.updateSeenCount(wp.id)
                }
                return wp
            } ?: kotlin.run {
                return getLocalWallpaper()
            }
        }
    }

    @Suppress("DeferredResultUnused")
    private suspend fun getLocalWallpaper(): Wallpaper? {
        Log.d("WallpaperProvider", "Getting from local cache")
        localDataSource.getWallpapers()?.let {
            if (it.isNotEmpty()) {
                val wp = it[0]
                localDataSource.updateSeenCount(wp.id)
                return wp
            }
        }
        Log.d("WallpaperProvider", "No cached wallpapers")
        return null
    }

}
