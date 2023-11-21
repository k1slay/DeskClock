package com.k2.deskclock.wallpaper.data.models

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import com.k2.deskclock.commons.GSON

@Entity(tableName = "WALLPAPERS")
@TypeConverters(Converters::class)
data class WallpaperCache(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "rawData") val wallpaper: Wallpaper,
    @ColumnInfo(name = "tags") val tags: String?,
    @ColumnInfo(name = "fetchedAt") val fetchedAt: Long,
    @ColumnInfo(name = "accessCount") val accessCount: Int = 0,
)

@Dao
interface WallpaperCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEntry(wallpaper: WallpaperCache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEntries(wallpapers: List<WallpaperCache>)

    @Query("SELECT * FROM WALLPAPERS WHERE tags LIKE :tags ORDER BY accessCount ASC, fetchedAt ASC")
    suspend fun getByTag(tags: String?): List<WallpaperCache>?

    @Query("SELECT * FROM WALLPAPERS WHERE id is :id")
    suspend fun getById(id: String): WallpaperCache?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEntry(wallpaper: WallpaperCache)

    @Query("SELECT COUNT() FROM WALLPAPERS WHERE accessCount = :limitCount")
    suspend fun unseenWallpaperCount(limitCount: Int = 0): Int

}

class Converters {
    @TypeConverter
    fun fromWallpaper(value: String): Wallpaper {
        return GSON.fromJson(value, Wallpaper::class.java)
    }

    @TypeConverter
    fun toWallpaper(data: Wallpaper): String {
        return GSON.toJson(data)
    }

}
