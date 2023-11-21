package com.k2.deskclock.weather.data.models

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.k2.deskclock.location.models.Location
import com.k2.deskclock.location.models.Place
import com.k2.deskclock.location.storage.RoomConverters
import com.k2.deskclock.weather.Constants.GSON
import com.k2.deskclock.weather.data.models.openMeteo.OpenMeteoResponse

@Entity(tableName = "WEATHER_CACHE")
@TypeConverters(Converters::class, RoomConverters::class)
data class WeatherLocalCache(
    @PrimaryKey val uid: Int = UID,
    @ColumnInfo(name = "raw_data") val data: OpenMeteoResponse,
    @ColumnInfo(name = "location") val location: Location,
    @ColumnInfo(name = "fetched_at") val fetchedAt: Long,
) {
    companion object {
        const val UID = 1
    }
}

@Entity(tableName = "ADDRESS_CACHE")
@TypeConverters(RoomConverters::class)
data class AddressLocalCache(
    @PrimaryKey val uid: Int = UID,
    @ColumnInfo(name = "address") val place: Place,
) {
    companion object {
        const val UID = 1
    }
}

@Dao
interface AddressCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToCache(place: AddressLocalCache)

    @Query("SELECT * FROM ADDRESS_CACHE WHERE uid = :uid")
    suspend fun getFromCache(uid: Int = AddressLocalCache.UID): AddressLocalCache?
}

@Dao
interface WeatherCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToCache(weatherLocalCache: WeatherLocalCache)

    @Query("SELECT * FROM WEATHER_CACHE WHERE uid = :uid")
    suspend fun getFromCache(uid: Int = WeatherLocalCache.UID): WeatherLocalCache?
}

class Converters {
    @TypeConverter
    fun fromOpenMeteoResponse(value: String): OpenMeteoResponse {
        return GSON.fromJson(value, OpenMeteoResponse::class.java)
    }

    @TypeConverter
    fun toOpenMeteoResponse(data: OpenMeteoResponse): String {
        return GSON.toJson(data)
    }

}
