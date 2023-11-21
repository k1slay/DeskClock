package com.k2.deskclock.weather.data.remote

import com.k2.deskclock.location.models.Location
import com.k2.deskclock.weather.data.WeatherDataSource
import com.k2.deskclock.weather.data.models.openMeteo.OpenMeteoResponse

interface WeatherRemoteDataSource : WeatherDataSource

class WeatherRemoteDataSourceImpl(
    private val weatherApi: WeatherApiInterface,
) : WeatherRemoteDataSource {
    override suspend fun getWeather(location: Location): OpenMeteoResponse? {
        return kotlin.runCatching {
            weatherApi.fetchWeather(
                location.lat,
                location.lng,
                location.timeZone,
            ).body()
        }.getOrNull()
    }
}
