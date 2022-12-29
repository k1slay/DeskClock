package com.k2.deskclock.weather.data.models

import androidx.annotation.DrawableRes
import com.k2.deskclock.location.models.Place

data class Weather(
    val currentTemp: Float,
    val dailyMaxTemp: Float,
    val dailyMinTemp: Float,
    val dailyPrecipitation: Float,
    val humidity: Int,
    val sunrise: Long,
    val sunset: Long,
    val lat: Double,
    val lng: Double,
    val temperatureUnit: String,
    val precipitationUnit: String,
    val humidityUnit: String,
    val userReadableDescription: String?,
    @DrawableRes val icon: Int?,
    val place: Place?,
    val forecast: List<Forecast>?
)

data class Forecast(
    val min: Float,
    val max: Float,
    val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val precipitation: Float,
    val temperatureUnit: String,
    val precipitationUnit: String,
    val humidityUnit: String,
    val userReadableDescription: String?,
    @DrawableRes val icon: Int?
)
