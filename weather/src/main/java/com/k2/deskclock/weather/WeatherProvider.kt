package com.k2.deskclock.weather

import com.k2.deskclock.location.AddressProvider
import com.k2.deskclock.location.models.Location
import com.k2.deskclock.location.models.Place
import com.k2.deskclock.weather.data.local.WeatherLocalDataSource
import com.k2.deskclock.weather.data.models.Weather
import com.k2.deskclock.weather.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface WeatherProvider {
    suspend fun getWeather(location: Location): Weather?
}

@Suppress("DeferredResultUnused")
class WeatherProviderImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val addressProvider: AddressProvider,
) : WeatherProvider {
    override suspend fun getWeather(location: Location): Weather? =
        withContext(Dispatchers.IO) {
            var locationChanged = false
            val cachedPlace: Place? = localDataSource.getCachedAddress()
            val place =
                if (addressProvider.isPlaceNearby(cachedPlace, location)) {
                    cachedPlace
                } else {
                    locationChanged = true
                    placeFromApi(location)
                }
            if (locationChanged) {
                weatherFromApi(location, place)
            } else {
                weatherFromCache(location, place) ?: kotlin.run {
                    weatherFromApi(location, place)
                }
            }
        }

    private suspend fun weatherFromCache(
        location: Location,
        place: Place?,
    ): Weather? {
        return localDataSource.getWeather(location)?.toWeather(place)
    }

    private suspend fun weatherFromApi(
        location: Location,
        place: Place?,
    ): Weather? {
        return remoteDataSource.getWeather(location)?.also {
            localDataSource.cacheWeatherAsync(it, location)
        }?.toWeather(place)
    }

    private suspend fun placeFromApi(location: Location): Place? {
        return addressProvider.fromLatLng(location.lat, location.lng)?.let { address ->
            Place(address, location).also {
                localDataSource.cacheAddressAsync(it)
            }
        }
    }
}
