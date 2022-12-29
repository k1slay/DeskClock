package com.k2.deskclock.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.k2.deskclock.R
import com.k2.deskclock.utils.getFormattedTime
import com.k2.deskclock.weather.data.models.Forecast
import com.k2.deskclock.weather.data.models.Weather
import java.lang.Integer.min
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Composable
fun Weather(weather: Weather, showForecast: Boolean) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            weather.icon?.let { icon ->
                Image(
                    painter = painterResource(icon),
                    contentDescription = weather.userReadableDescription,
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colors.onBackground.copy(0.85F)
                    ),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            WeatherText(weather)
        }
        Spacer(modifier = Modifier.size(8.dp))
        weather.userReadableDescription?.let {
            WeatherTextView(
                text = it,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        WeatherTextView(
            text = "Lows: " +
                "${weather.dailyMinTemp.roundToInt()} ${weather.temperatureUnit} - " +
                "Highs: " +
                "${weather.dailyMaxTemp.roundToInt()} ${weather.temperatureUnit}",
            fontSize = 16.sp
        )
        if (showForecast) {
            weather.forecast?.let { Forecast(forecast = it) }
        }
    }
}

@Composable
private fun WeatherText(weather: Weather) {
    Text(
        buildAnnotatedString {
            append(weather.currentTemp.roundToInt().toString())
            withStyle(style = SpanStyle(fontSize = 20.sp)) {
                append(" ${weather.temperatureUnit}")
            }
        },
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        modifier = Modifier.alpha(0.85F)
    )
}

@Composable
private fun Forecast(forecast: List<Forecast>) {
    Spacer(modifier = Modifier.size(30.dp))
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val count = min(5, forecast.size - 1)
        items(count) { index ->
            Spacer(modifier = Modifier.size(10.dp))
            ForecastItem(forecast = forecast[index + 1])
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}

@Composable
private fun ForecastItem(forecast: Forecast) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(forecast.icon ?: R.drawable.ic_sunny),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                MaterialTheme.colors.onBackground.copy(0.85F)
            ),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        WeatherTextView(
            text = "${forecast.min.roundToInt()} - ${forecast.max.roundToInt()} ${forecast.temperatureUnit}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(4.dp))
        WeatherTextView(
            text = TimeUnit.SECONDS.toMillis(forecast.date).getFormattedTime("EEE"),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun WeatherTextView(
    text: String,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Text(
        text = text,
        fontSize = fontSize,
        modifier = Modifier.alpha(0.85F),
        fontWeight = fontWeight
    )
}
