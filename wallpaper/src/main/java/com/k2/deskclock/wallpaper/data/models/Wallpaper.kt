package com.k2.deskclock.wallpaper.data.models

data class Wallpaper(
    val path: String,
    val id: String,
    val credit: Credit,
)

data class Credit(
    val websiteName: String,
    val websiteUrl: String,
    val userName: String,
    val userUrl: String,
)
