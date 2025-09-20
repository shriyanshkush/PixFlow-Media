package com.example.pexel.domain.repository

import com.example.pexel.data.local.entities.FavoriteEntity
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.data.remote.models.video.PexelVideo
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getAllFavorites(): Flow<List<FavoriteEntity>>
    fun getFavoritesByType(type: String): Flow<List<FavoriteEntity>>
    fun isFavorite(id: String): Flow<Boolean>
    suspend fun addPhotoToFavorites(photo: PexelsPhoto)
    suspend fun addVideoToFavorites(video: PexelVideo)
    suspend fun removeFavorite(id: String)
    suspend fun updateDownloadStatus(id: String, isDownloaded: Boolean, localPath: String?)
    fun getFavoritesCount(): Flow<Int>
}