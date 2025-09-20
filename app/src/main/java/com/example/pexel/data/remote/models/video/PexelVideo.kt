// Replace your current video models with these exact models
// The key is using property names that match the JSON exactly

package com.example.pexel.data.remote.models.video

import kotlinx.serialization.Serializable

@Serializable
data class PexelVideo(
    val id: Int,
    val width: Int,
    val height: Int,
    val duration: Int,
    val full_res: String? = null,
    val tags: List<String> = emptyList(),
    val url: String,
    val image: String,
    val avg_color: String? = null,
    val user: VideoUser,
    val video_files: List<VideoFile>? = null,
    val video_pictures: List<VideoPicture>? = null
) {
    // Computed properties for your existing code
    val videoFiles: List<VideoFile>? get() = video_files
    val videoPictures: List<VideoPicture>? get() = video_pictures
    val fullRes: String? get() = full_res
    val avgColor: String? get() = avg_color
}

@Serializable
data class VideoUser(
    val id: Int,
    val name: String,
    val url: String
)

@Serializable
data class VideoFile(
    val id: Int,
    val quality: String,
    val file_type: String,
    val width: Int? = null,
    val height: Int? = null,
    val fps: Float? = null,
    val link: String,
    val size: Long? = null
) {
    // Computed property for your existing code
    val fileType: String get() = file_type
}

@Serializable
data class VideoPicture(
    val id: Int,
    val picture: String,
    val nr: Int
)