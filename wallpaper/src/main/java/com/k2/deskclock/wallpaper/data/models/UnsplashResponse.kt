package com.k2.deskclock.wallpaper.data.models

import android.net.Uri
import android.os.Build
import com.google.gson.annotations.SerializedName
import com.k2.deskclock.wallpaper.data.Constants
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

data class UnsplashResponse(
    @SerializedName("urls") val urls: Urls,
    @SerializedName("id") val id: String,
    @SerializedName("user") val user: User,
)

data class User(
    @SerializedName("name") val name: String,
    @SerializedName("username") val profile: String,
)

data class Urls(
    @SerializedName("regular") val regular: String,
    @SerializedName("full") val full: String
)

fun UnsplashResponse.toWallpaper(): Wallpaper {
    return Wallpaper(this.urls.regular, this.id, this.makeCredit())
}

fun UnsplashResponse.makeCredit(): Credit {
    return Credit(
        websiteName = Constants.WALLPAPER_SOURCE_NAME,
        websiteUrl = Constants.BASE_UNSPLASH_URL.addUtmParams,
        userName = this.user.name,
        userUrl = "${Constants.BASE_UNSPLASH_URL}@${this.user.profile}".addUtmParams
    )
}

private val String.addUtmParams: String
    get() = Uri.Builder()
        .encodedPath(this)
        .appendQueryParameter(Constants.UTM_SOURCE_KEY, Constants.UTM_SOURCE_VAL.encode)
        .appendQueryParameter(Constants.UTM_MEDIUM_KEY, Constants.UTM_MEDIUM_VAL.encode)
        .toString()

private val String.encode: String
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            URLEncoder.encode(this, UTF_8.name())
        } else {
            URLEncoder.encode(this)
        }
    }

fun List<UnsplashResponse>.toWallpapers(): List<Wallpaper> {
    val result = mutableListOf<Wallpaper>()
    for (wp in this) {
        result.add(wp.toWallpaper())
    }
    return result
}
