package com.example.pexel.domain.models

import kotlinx.serialization.Serializable

data class Photo(
    val id: Int,
    val url: String,
    val photographer: String,
    val src: PhotoSource,
    val avgColor: String,
    val alt: String
)


@Serializable
data class PhotoSource(
    val original: String, // For download
    val large: String,    // For detail screen
    val medium: String    // For grid/preview
)
