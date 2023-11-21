package com.k2.deskclock.wallpaper.data.remote

import com.k2.deskclock.wallpaper.data.models.UnsplashResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpaperApiInterface {
    @GET("/photos/random/")
    suspend fun fetchWallpaper(
        @Query("client_id") apiKey: String,
        @Query("page") page: String = "1",
        @Query("per_page") perPage: String = "1",
        @Query("orientation") orientation: String = "landscape",
        @Query("featured") featured: String = "true",
        @Query("query") category: String = "longexposure",
        @Query("count") count: Int = 25,
    ): Response<List<UnsplashResponse>>

}
