package com.k2.deskclock.commons.preferences

import android.content.SharedPreferences

interface PreferenceStore {
    var clockSize: Float
    var darkTheme: Boolean
    var wallpaperCategory: String
    var homeMode: String
}

class PreferenceStoreImpl(
    private val preferences: SharedPreferences,
) : PreferenceStore {
    override var clockSize: Float
        get() = preferences.getFloat(PreferenceKeys.CLOCK_SIZE, FONT_SIZE)
        set(value) {
            preferences.edit().putFloat(PreferenceKeys.CLOCK_SIZE, value).apply()
        }

    override var darkTheme: Boolean
        get() = preferences.getBoolean(PreferenceKeys.DARK_THEME, DARK_THEME)
        set(value) {
            preferences.edit().putBoolean(PreferenceKeys.DARK_THEME, value).apply()
        }

    override var wallpaperCategory: String
        get() = preferences.getString(PreferenceKeys.WALLPAPER_CATEGORIES, null) ?: CATEGORY
        set(value) {
            preferences.edit().putString(PreferenceKeys.WALLPAPER_CATEGORIES, value).apply()
        }

    override var homeMode: String
        get() = preferences.getString(PreferenceKeys.HOME_MODE, HOME) ?: HOME
        set(value) {
            preferences.edit().putString(PreferenceKeys.HOME_MODE, value).apply()
        }

    companion object Defaults {
        const val FONT_SIZE = 64F
        const val DARK_THEME = true
        const val HOME = "ambient"
        const val CATEGORY = "nightsky"
    }
}
