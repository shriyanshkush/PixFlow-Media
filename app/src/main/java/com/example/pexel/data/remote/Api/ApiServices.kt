package com.example.pexel.data.remote.Api

import com.example.pexel.data.remote.models.photo.PexelPhotoResponse
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.data.remote.models.video.PexelVideo
import com.example.pexel.data.remote.models.video.PexelVideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    // 🔹 Popular Photos
    @GET("v1/curated")
    suspend fun getPopularImages(
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): PexelPhotoResponse

    // 🔹 Search Photos
    @GET("v1/search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): PexelPhotoResponse

    // 🔹 Popular Videos
    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): PexelVideoResponse

    // 🔹 Search Videos
    @GET("videos/search")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): PexelVideoResponse

    @GET("v1/photos/{id}")
    suspend fun getPhotoById(@Path("id") photoId: String): PexelsPhoto

    @GET("videos/videos/{id}")
    suspend fun getVideoById(@Path("id") videoId: String): PexelVideo
}
