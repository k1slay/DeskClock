package com.k2.deskclock.location.di

import com.k2.deskclock.location.AddressProvider
import com.k2.deskclock.location.LocationProvider
import com.k2.deskclock.location.geocoding.AddressProviderImpl
import com.k2.deskclock.location.provider.LocationProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    internal abstract fun bindAnalyticsService(
        locationProvider: AddressProviderImpl
    ): AddressProvider

    @Binds
    internal abstract fun bindLocationProvider(
        locationProvider: LocationProviderImpl
    ): LocationProvider

}
