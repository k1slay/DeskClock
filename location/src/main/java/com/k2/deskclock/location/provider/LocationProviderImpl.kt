package com.k2.deskclock.location.provider

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.k2.deskclock.location.LocationListener
import com.k2.deskclock.location.LocationProvider
import com.k2.deskclock.location.models.ErrorType
import com.k2.deskclock.location.models.LocationError
import com.k2.deskclock.location.models.toLocation
import com.k2.deskclock.location.utils.hasLocationPermission
import com.k2.deskclock.location.utils.isLocationEnabled
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class LocationProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider {

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(locationListener: LocationListener) {
        if (context.hasLocationPermission.not()) {
            onNoPermission(locationListener)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
            location?.let { gmsLocation ->
                locationListener.onLocation(gmsLocation.toLocation)
            } ?: kotlin.run {
                onError(locationListener, null)
            }
        }.addOnFailureListener {
            onError(locationListener, it)
        }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(locationListener: LocationListener) {
        if (context.hasLocationPermission.not()) {
            onNoPermission(locationListener)
            return
        }
        if (context.isLocationEnabled().not()) {
            locationListener.onError(
                LocationError(
                    message = "Location disabled",
                    code = ErrorType.LocationDisabled
                )
            )
        }
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    return this
                }

                override fun isCancellationRequested() = false
            }
        )
            .addOnSuccessListener { location ->
                location?.let {
                    locationListener.onLocation(location = location.toLocation)
                } ?: kotlin.run {
                    onError(locationListener, null)
                }
            }
            .addOnFailureListener {
                onError(locationListener, it)
            }
    }

    private fun onNoPermission(locationListener: LocationListener) {
        Log.d("Location", "Permission denied")
        locationListener.onError(
            LocationError(
                message = "No permission",
                code = ErrorType.NoPermission
            )
        )
    }

    private fun onError(locationListener: LocationListener, throwable: Throwable?) {
        val message = throwable?.let {
            throwable.message ?: throwable.toString()
        } ?: kotlin.run {
            "Unknown"
        }
        locationListener.onError(
            LocationError(
                message = message,
                code = ErrorType.Unknown
            )
        )
    }

    private val fusedLocationClient: FusedLocationProviderClient
        get() = LocationServices.getFusedLocationProviderClient(
            context.applicationContext
        )

}
