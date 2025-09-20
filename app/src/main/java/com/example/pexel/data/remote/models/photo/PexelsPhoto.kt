package com.example.pexel.data.remote.models.photo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PexelsPhoto(
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    @SerialName("photographer_url") val photographerUrl: String,
    @SerialName("photographer_id") val photographerId: Long,
    @SerialName("avg_color") val avgColor: String,
    val src: PhotoSource,
    val liked: Boolean,
    val alt: String
)

@Serializable
data class PhotoSource(
    val original: String, // For download
    val large: String,    // For detail screen
    val medium: String    // For grid/preview
)

