package com.k2.deskclock.location.storage

import androidx.room.TypeConverter
import com.k2.deskclock.commons.GSON
import com.k2.deskclock.location.models.Location
import com.k2.deskclock.location.models.Place

class RoomConverters {
    @TypeConverter
    fun fromLocation(value: String): Location {
        return GSON.fromJson(value, Location::class.java)
    }

    @TypeConverter
    fun toLocation(location: Location): String {
        return GSON.toJson(location)
    }

    @TypeConverter
    fun fromPlace(value: String): Place? {
        return GSON.fromJson(value, Place::class.java)
    }

    @TypeConverter
    fun toPlace(place: Place?): String {
        return GSON.toJson(place)
    }

}
