package com.k2.deskclock.weather.data.local

import com.k2.deskclock.location.models.Location
import com.k2.deskclock.location.models.Place
import com.k2.deskclock.weather.data.WeatherDataSource
import com.k2.deskclock.weather.data.models.AddressCacheDao
import com.k2.deskclock.weather.data.models.AddressLocalCache
import com.k2.deskclock.weather.data.models.WeatherCacheDao
import com.k2.deskclock.weather.data.models.WeatherLocalCache
import com.k2.deskclock.weather.data.models.openMeteo.OpenMeteoResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.TimeUnit.MILLISECONDS

interface WeatherLocalDataSource : WeatherDataSource {

    fun cacheWeatherAsync(
        openMeteoResponse: OpenMeteoResponse,
        location: Location
    ): Deferred<Unit>

    fun cacheAddressAsync(
        place: Place
    ): Deferred<Unit>

    suspend fun getCachedAddress(): Place?
}

@DelicateCoroutinesApi
class WeatherLocalDataSourceImpl(
    private val weatherCacheDao: WeatherCacheDao,
    private val addressCacheDao: AddressCacheDao
) : WeatherLocalDataSource {

    @OptIn(DelicateCoroutinesApi::class)
    override fun cacheWeatherAsync(
        openMeteoResponse: OpenMeteoResponse,
        location: Location
    ) = GlobalScope.async {
        weatherCacheDao.saveToCache(
            WeatherLocalCache(
                data = openMeteoResponse,
                location = location,
                fetchedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun getWeather(location: Location): OpenMeteoResponse? {
        val cachedData: WeatherLocalCache = weatherCacheDao.getFromCache() ?: return null
        val elapsedSinceFetch = System.currentTimeMillis() - cachedData.fetchedAt
        val cacheLocation = cachedData.location
        return if (MILLISECONDS.toDays(elapsedSinceFetch) > 1 && cacheLocation == location) {
            null
        } else {
            cachedData.data
        }
    }

    override suspend fun getCachedAddress(): Place? {
        return addressCacheDao.getFromCache()?.place
    }

    override fun cacheAddressAsync(place: Place) = GlobalScope.async {
        addressCacheDao.saveToCache(AddressLocalCache(place = place))
    }
}
