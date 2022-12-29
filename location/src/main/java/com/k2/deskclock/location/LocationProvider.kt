package com.k2.deskclock.location

import com.k2.deskclock.location.models.Location
import com.k2.deskclock.location.models.LocationError

interface LocationProvider {
    fun getLastKnownLocation(locationListener: LocationListener)
    fun getCurrentLocation(locationListener: LocationListener)
}

interface LocationListener {
    fun onLocation(location: Location)
    fun onError(error: LocationError)
}
