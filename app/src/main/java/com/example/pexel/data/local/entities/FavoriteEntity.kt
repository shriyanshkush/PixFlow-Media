package com.example.pexel.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val id: String,
    val type: String, // "photo" or "video"
    val title: String,
    val creator: String,
    val imageUrl: String,
    val originalUrl: String,
    val width: Int,
    val height: Int,
    val duration: Int? = null, // Only for videos
    val avgColor: String? = null, // Only for photos
    val videoUrls: String? = null, // JSON string of video URLs
    val dateAdded: Long = System.currentTimeMillis(),
    val isDownloaded: Boolean = false,
    val localPath: String? = null
)