package com.k2.deskclock.location.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat

inline val Context.hasLocationPermission: Boolean
    get() = this.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.isLocationEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (
        locationManager.isProviderEnabled(LocationManager.FUSED_PROVIDER) ||
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    ) {
        return true
    }
    return false
}
