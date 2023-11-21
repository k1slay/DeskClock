package com.k2.deskclock.weather.data.local

import com.k2.deskclock.weather.data.models.AddressCacheDao
import com.k2.deskclock.weather.data.models.WeatherCacheDao

interface WeatherDaoProvider {
    fun weatherCacheDao(): WeatherCacheDao

    fun addressCacheDao(): AddressCacheDao
}
