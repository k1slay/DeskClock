package com.k2.deskclock.weather.data.remote

import com.k2.deskclock.weather.data.models.openMeteo.OpenMeteoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiInterface {
    @GET("v1/forecast")
    suspend fun fetchWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double,
        @Query("timezone") tz: String,
        @Query("hourly") hourly: Array<String> = hourlyParams,
        @Query("daily") daily: Array<String> = dailyParams,
        @Query("timeformat") timeFormat: String = "unixtime",
    ): Response<OpenMeteoResponse>

    companion object Params {
        private val hourlyParams = arrayOf("temperature_2m", "relativehumidity_2m")
        private val dailyParams =
            arrayOf(
                "weathercode",
                "sunrise",
                "sunset",
                "precipitation_sum",
                "temperature_2m_max",
                "temperature_2m_min",
            )
    }
}
