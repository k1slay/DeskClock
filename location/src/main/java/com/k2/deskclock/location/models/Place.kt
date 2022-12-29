package com.k2.deskclock.location.models

import android.location.Address

data class Place(
    val area: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    val postalCode: String?,
    val latitude: Double,
    val longitude: Double
) {
    constructor(address: Address, location: Location) : this(
        address.subLocality,
        address.locality,
        address.adminArea,
        address.countryName,
        address.postalCode,
        location.lat,
        location.lng
    )
}
