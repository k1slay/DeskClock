package com.k2.deskclock.location.geocoding

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.k2.deskclock.commons.di.IoDispatcher
import com.k2.deskclock.location.AddressProvider
import com.k2.deskclock.location.MIN_DISTANCE_FOR_REFRESH
import com.k2.deskclock.location.models.Location
import com.k2.deskclock.location.models.Place
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AddressProviderImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : AddressProvider {
        private val geocoder: Geocoder by lazy {
            Geocoder(context)
        }

        override suspend fun fromLatLng(
            lat: Double,
            lng: Double,
        ): Address? =
            withContext(ioDispatcher) {
                if (Geocoder.isPresent().not()) {
                    return@withContext null
                }
                kotlin.runCatching {
                    geocoder.getFromLocation(lat, lng, 1)?.let { addresses ->
                        addresses[0]
                    }
                }.getOrNull()
            }

        override suspend fun distanceBetween(
            location: Location,
            place: Place,
        ): Float {
            val loc1 =
                android.location.Location(location.toString()).apply {
                    latitude = location.lat
                    longitude = location.lng
                }
            val loc2 =
                android.location.Location(place.toString()).apply {
                    latitude = place.latitude
                    longitude = place.longitude
                }
            return loc1.distanceTo(loc2)
        }

        override suspend fun isPlaceNearby(
            place: Place?,
            location: Location,
        ): Boolean {
            place ?: return false
            return distanceBetween(location, place) < MIN_DISTANCE_FOR_REFRESH
        }

    }
