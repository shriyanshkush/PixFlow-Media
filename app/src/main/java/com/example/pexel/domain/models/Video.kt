package com.example.pexel.domain.models

data class Video(
    val id: Int,
    val url: String,
    val user: String,
    val videoUrl: String,  // pick best quality for view
    val downloadUrl: String // pick HD for download
)
