package com.k2.deskclock.weather.data

import com.k2.deskclock.location.models.Location
import com.k2.deskclock.weather.data.models.openMeteo.OpenMeteoResponse

interface WeatherDataSource {
    suspend fun getWeather(location: Location): OpenMeteoResponse?
}
