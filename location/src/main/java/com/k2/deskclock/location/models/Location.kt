package com.k2.deskclock.location.models

import java.util.TimeZone

data class Location(
    val lat: Double,
    val lng: Double,
    val timeZone: String,
)

internal val android.location.Location.toLocation
    get() =
        Location(
            lat = latitude,
            lng = longitude,
            timeZone = TimeZone.getDefault().id,
        )
