package com.k2.deskclock.ui

import androidx.compose.runtime.MutableState
import com.k2.deskclock.location.models.ErrorType
import com.k2.deskclock.ui.widgets.NavTarget
import com.k2.deskclock.wallpaper.data.models.Wallpaper
import com.k2.deskclock.weather.data.models.Weather
import java.util.concurrent.TimeUnit

interface UiStateHolder {
    val weather: MutableState<Weather?>
    val wallpaper: MutableState<Wallpaper?>
    val insetVisible: MutableState<Boolean>
    val darkTheme: MutableState<Boolean>
    val clockSize: MutableState<Float>
    val time: MutableState<Long>
    val locationError: MutableState<ErrorType?>
    val navState: MutableState<NavTarget>

    companion object {
        val PERIODIC_REFRESH_INTERVAL = TimeUnit.HOURS.toMillis(1)
    }

}
