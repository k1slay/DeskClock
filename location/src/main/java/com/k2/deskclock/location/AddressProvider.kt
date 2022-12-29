package com.k2.deskclock.location

import android.location.Address
import com.k2.deskclock.location.models.Location
import com.k2.deskclock.location.models.Place

interface AddressProvider {
    suspend fun fromLatLng(lat: Double, lng: Double): Address?
    suspend fun distanceBetween(location: Location, place: Place): Float?
    suspend fun isPlaceNearby(place: Place?, location: Location): Boolean
}
