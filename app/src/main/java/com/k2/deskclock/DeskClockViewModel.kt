package com.k2.deskclock

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k2.deskclock.commons.di.IoDispatcher
import com.k2.deskclock.commons.preferences.PreferenceStore
import com.k2.deskclock.location.LocationListener
import com.k2.deskclock.location.LocationProvider
import com.k2.deskclock.location.models.ErrorType
import com.k2.deskclock.location.models.Location
import com.k2.deskclock.location.models.LocationError
import com.k2.deskclock.ui.UiStateHolder
import com.k2.deskclock.ui.widgets.NavTarget.Settings.fromName
import com.k2.deskclock.wallpaper.WallpaperProvider
import com.k2.deskclock.wallpaper.data.models.Wallpaper
import com.k2.deskclock.weather.WeatherProvider
import com.k2.deskclock.weather.data.models.Weather
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeskClockViewModel @Inject constructor(
    private val weatherProvider: Lazy<WeatherProvider>,
    private val wallpaperProvider: Lazy<WallpaperProvider>,
    val preferenceStore: PreferenceStore,
    private val locationProvider: LocationProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), UiStateHolder {

    override val weather: MutableState<Weather?> = mutableStateOf(null)
    override val wallpaper: MutableState<Wallpaper?> = mutableStateOf(null)
    override val insetVisible: MutableState<Boolean> = mutableStateOf(true)
    override val darkTheme: MutableState<Boolean> = mutableStateOf(preferenceStore.darkTheme)
    override val clockSize: MutableState<Float> = mutableStateOf(preferenceStore.clockSize)
    override val time: MutableState<Long> = mutableStateOf(System.currentTimeMillis())
    override val locationError: MutableState<ErrorType?> = mutableStateOf(null)
    override val navState = mutableStateOf(preferenceStore.homeMode.fromName)
    var lastUpdatedAt = System.currentTimeMillis()
        private set

    fun refresh() {
        lastUpdatedAt = System.currentTimeMillis()
        fetchLocationAndWeather()
        fetchWallpaper()
    }

    fun fetchWallpaper() = viewModelScope.launch(ioDispatcher) {
        wallpaper.value = wallpaperProvider.get().fetchNextWallpaper()
    }

    @SuppressLint("MissingPermission")
    fun fetchLocationAndWeather() {
        locationProvider.getLastKnownLocation(object : LocationListener {
            override fun onLocation(location: Location) {
                fetchWeather(location = location)
            }

            override fun onError(error: LocationError) {
                if (error.code == ErrorType.Unknown) {
                    Log.d("Location", "No last known location, Requesting location update")
                    requestLocationUpdate()
                } else {
                    Log.e("Location", error.toString())
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        locationProvider.getCurrentLocation(object : LocationListener {
            override fun onLocation(location: Location) {
                fetchWeather(location = location)
            }

            override fun onError(error: LocationError) {
                if (error.code == ErrorType.LocationDisabled) {
                    locationError.value = error.code
                }
                Log.e("Location", error.toString())
            }
        })
    }

    private fun fetchWeather(location: Location) = viewModelScope.launch(ioDispatcher) {
        weather.value = weatherProvider.get().getWeather(location)
        Log.d("Weather", weather.value.toString())
    }

}
