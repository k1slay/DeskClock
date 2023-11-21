package com.k2.deskclock.wallpaper.data.local

import com.k2.deskclock.commons.preferences.PreferenceStore
import com.k2.deskclock.wallpaper.data.WallpaperDataSource
import com.k2.deskclock.wallpaper.data.models.Wallpaper
import com.k2.deskclock.wallpaper.data.models.WallpaperCache
import com.k2.deskclock.wallpaper.data.models.WallpaperCacheDao

interface WallpaperLocalDataSource : WallpaperDataSource {
    suspend fun cacheWallpaper(wallpapers: List<Wallpaper>)

    suspend fun updateSeenCount(wallpaperId: String)

    suspend fun hasUnseenWallpapers(): Boolean
}

class WallpaperLocalDataSourceImpl(
    private val dao: WallpaperCacheDao,
    private val preferenceStore: PreferenceStore,
) : WallpaperLocalDataSource {
    override suspend fun cacheWallpaper(wallpapers: List<Wallpaper>) {
        val entities = mutableListOf<WallpaperCache>()
        for (wallpaper in wallpapers) {
            WallpaperCache(
                id = wallpaper.id,
                wallpaper = wallpaper,
                tags = preferenceStore.wallpaperCategory,
                fetchedAt = System.currentTimeMillis(),
            ).also {
                entities.add(it)
            }
        }
        dao.addEntries(entities)
    }

    override suspend fun getWallpapers(): List<Wallpaper>? {
        val tag = preferenceStore.wallpaperCategory
        return dao.getByTag(tag)?.map {
            it.wallpaper
        }
    }

    override suspend fun updateSeenCount(wallpaperId: String) {
        dao.getById(wallpaperId)?.let {
            dao.updateEntry(it.copy(accessCount = it.accessCount + 1))
        }
    }

    override suspend fun hasUnseenWallpapers(): Boolean {
        return dao.unseenWallpaperCount() > 0
    }

}
