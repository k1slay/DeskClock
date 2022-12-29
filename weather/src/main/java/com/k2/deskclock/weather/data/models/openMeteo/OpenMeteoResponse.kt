package com.k2.deskclock.weather.data.models.openMeteo

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.k2.deskclock.location.models.Place
import com.k2.deskclock.weather.R
import com.k2.deskclock.weather.data.models.Forecast
import com.k2.deskclock.weather.data.models.Weather
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

@Keep
data class OpenMeteoResponse(
    @SerializedName("elevation") val elevation: Float,
    @SerializedName("hourly") val hourly: Hourly,
    @SerializedName("daily") val daily: Daily,
    @SerializedName("latitude") val lat: Double,
    @SerializedName("longitude") val lng: Double,
    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Long,
    @SerializedName("daily_units") val dailyUnits: DailyUnits,
    @SerializedName("hourly_units") val hourlyUnits: HourlyUnits
) {
    fun toWeather(place: Place?): Weather {
        val curTimeSecs = MILLISECONDS.toSeconds(System.currentTimeMillis())
        val dailyIndex = daily.timeList.getCurrentTimeIndex(curTimeSecs)
        val hourlyIndex = hourly.timeList.getCurrentTimeIndex(curTimeSecs)
        return Weather(
            hourly.temperature[hourlyIndex],
            daily.maxTemp[dailyIndex],
            daily.minTemp[dailyIndex],
            daily.precipitation[dailyIndex] ?: 0F,
            hourly.humidity[hourlyIndex],
            daily.sunrise[dailyIndex],
            daily.sunset[dailyIndex],
            lat,
            lng,
            hourlyUnits.temp,
            dailyUnits.precipitation,
            hourlyUnits.humidity,
            wmoCodeToText(daily.weatherCode[dailyIndex]),
            wmoCodeToIcon(daily.weatherCode[dailyIndex], curTimeSecs > daily.sunset[dailyIndex]),
            place,
            makeForecast(this, curTimeSecs)
        )
    }
}

private fun makeForecast(weather: OpenMeteoResponse, curTimeInSecs: Long): List<Forecast> {
    val daily = weather.daily
    val list = mutableListOf<Forecast>()
    val today = curTimeInSecs.toDateString
    for ((index, day) in daily.timeList.withIndex()) {
        if (today != day.toDateString) {
            Forecast(
                daily.minTemp[index],
                daily.maxTemp[index],
                day,
                daily.sunrise[index],
                daily.sunset[index],
                daily.precipitation[index] ?: 0F,
                weather.hourlyUnits.temp,
                weather.dailyUnits.precipitation,
                weather.dailyUnits.precipitation,
                wmoCodeToText(daily.weatherCode[index]),
                wmoCodeToIcon(daily.weatherCode[index], false)
            ).also {
                list.add(it)
            }
        }
    }
    return list
}

private val Long.toDateString: String
    @SuppressLint("SimpleDateFormat")
    get() {
        val format = SimpleDateFormat("dd MM yyyy")
        return format.format(Date(TimeUnit.SECONDS.toMillis(this)))
    }

@Keep
data class HourlyUnits(
    @SerializedName("time") val time: String,
    @SerializedName("temperature_2m") val temp: String,
    @SerializedName("relativehumidity_2m") val humidity: String
)

@Keep
data class DailyUnits(
    @SerializedName("sunset") val sunset: String,
    @SerializedName("time") val time: String,
    @SerializedName("weathercode") val weatherCode: String,
    @SerializedName("sunrise") val sunrise: String,
    @SerializedName("precipitation_sum") val precipitation: String
)

@Keep
data class Daily(
    @SerializedName("time") val timeList: List<Long>,
    @SerializedName("sunset") val sunset: List<Long>,
    @SerializedName("sunrise") val sunrise: List<Long>,
    @SerializedName("weathercode") val weatherCode: List<Int>,
    @SerializedName("precipitation_sum") val precipitation: List<Float?>,
    @SerializedName("temperature_2m_max") val maxTemp: List<Float>,
    @SerializedName("temperature_2m_min") val minTemp: List<Float>
)

@Keep
data class Hourly(
    @SerializedName("time") val timeList: List<Long>,
    @SerializedName("temperature_2m") val temperature: List<Float>,
    @SerializedName("relativehumidity_2m") val humidity: List<Int>
)

private fun List<Long>.getCurrentTimeIndex(current: Long): Int {
    forEachIndexed { index, number ->
        if (number > current) {
            return if (index <= 0) {
                index
            } else {
                index - 1
            }
        }
    }
    return 0
}

private fun wmoCodeToText(code: Int): String? {
    return when (code) {
        0 -> "Clear"
        1, 2, 3 -> "Mostly clear"
        45, 48 -> "Foggy"
        51, 53, 55 -> "Drizzle"
        56, 57 -> "Freezing Drizzle"
        61, 63, 65 -> "Raining"
        66, 67 -> "Freezing Rain"
        71, 73, 75 -> "Snowing"
        77 -> "Snowy"
        80, 81, 82 -> "Rainy"
        85, 86 -> "Snow showers"
        95 -> "Thunderstorm"
        96, 99 -> "Thunderstorm with hail"
        else -> null
    }
}

fun wmoCodeToIcon(code: Int, night: Boolean): Int? {
    return when (code) {
        0, 1, 2, 3 -> if (night) R.drawable.weather_night_clear else R.drawable.weather_sunny
        45, 48 -> R.drawable.weather_fog
        51, 53, 55 -> R.drawable.weather_showers
        56, 57 -> R.drawable.weather_showers
        61, 63, 65 -> R.drawable.weather_rain
        66, 67 -> R.drawable.weather_rain
        71, 73, 75 -> R.drawable.weather_snow
        77 -> R.drawable.weather_snow
        80, 81, 82 -> R.drawable.weather_showers
        85, 86 -> R.drawable.weather_snow
        95 -> R.drawable.weather_thunderstorm
        96, 99 -> R.drawable.weather_thunderstorm
        else -> null
    }
}
