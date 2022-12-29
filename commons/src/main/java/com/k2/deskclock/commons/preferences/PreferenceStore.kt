package com.k2.deskclock.commons.preferences

import android.content.SharedPreferences

interface PreferenceStore {
    var clockSize: Float
    var darkTheme: Boolean
    var wallpaperCategory: String
    var homeMode: String
}

class PreferenceStoreImpl(
    private val preferences: SharedPreferences
) : PreferenceStore {

    override var clockSize: Float
        get() = preferences.getFloat(PreferenceKeys.CLOCK_SIZE, fontSize)
        set(value) {
            preferences.edit().putFloat(PreferenceKeys.CLOCK_SIZE, value).apply()
        }

    override var darkTheme: Boolean
        get() = preferences.getBoolean(PreferenceKeys.DARK_THEME, Defaults.darkTheme)
        set(value) {
            preferences.edit().putBoolean(PreferenceKeys.DARK_THEME, value).apply()
        }

    override var wallpaperCategory: String
        get() = preferences.getString(PreferenceKeys.WALLPAPER_CATEGORIES, null) ?: category
        set(value) {
            preferences.edit().putString(PreferenceKeys.WALLPAPER_CATEGORIES, value).apply()
        }

    override var homeMode: String
        get() = preferences.getString(PreferenceKeys.HOME_MODE, home) ?: home
        set(value) {
            preferences.edit().putString(PreferenceKeys.HOME_MODE, value).apply()
        }

    companion object Defaults {
        const val fontSize = 64F
        const val darkTheme = true
        const val home = "ambient"
        const val category = "nightsky"
    }
}
