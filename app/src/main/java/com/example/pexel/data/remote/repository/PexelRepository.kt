package com.example.pexel.data.remote.repository

import androidx.paging.PagingData
import com.example.pexel.data.remote.models.photo.PexelPhotoResponse
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.data.remote.models.video.PexelVideo
import com.example.pexel.data.remote.models.video.PexelVideoResponse
import kotlinx.coroutines.flow.Flow

interface PexelRepository {
    // Legacy methods (suspend functions) - keep for backward compatibility
    suspend fun getPopularImages(): PexelPhotoResponse
    suspend fun getPopularVideos(): PexelVideoResponse
    suspend fun searchPhotos(query: String): PexelPhotoResponse
    suspend fun searchVideos(query: String): PexelVideoResponse
    suspend fun getPhotoById(photoId: String): PexelsPhoto
    suspend fun getVideoById(videoId: String): PexelVideo

    // Paging 3 methods (Flow<PagingData>) - different names to avoid conflicts
    fun getPopularPhotosFlow(): Flow<PagingData<PexelsPhoto>>
    fun getPopularVideosFlow(): Flow<PagingData<PexelVideo>>
    fun searchPhotosFlow(query: String): Flow<PagingData<PexelsPhoto>>
    fun searchVideosFlow(query: String): Flow<PagingData<PexelVideo>>
}