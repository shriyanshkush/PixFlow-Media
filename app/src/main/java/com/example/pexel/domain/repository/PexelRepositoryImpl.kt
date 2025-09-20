// data/remote/repository/PexelRepositoryImpl.kt
package com.example.pexel.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pexel.data.remote.Api.ApiServices
import com.example.pexel.data.remote.models.photo.PexelPhotoResponse
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.data.remote.models.video.PexelVideo
import com.example.pexel.data.remote.models.video.PexelVideoResponse
import com.example.pexel.data.remote.paging.PhotosPagingSource
import com.example.pexel.data.remote.paging.VideosPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PexelRepositoryImpl @Inject constructor(
    private val apiService: ApiServices
) : PexelRepository {

    // Legacy methods (keep for backward compatibility)
    override suspend fun getPopularImages(): PexelPhotoResponse {
        return apiService.getPopularImages()
    }

    override suspend fun getPopularVideos(): PexelVideoResponse {
        return apiService.getPopularVideos()
    }

    override suspend fun searchPhotos(query: String): PexelPhotoResponse {
        return apiService.searchPhotos(query)
    }

    override suspend fun searchVideos(query: String): PexelVideoResponse {
        return apiService.searchVideos(query)
    }

    // Paging 3 methods
    override fun getPopularPhotosFlow(): Flow<PagingData<PexelsPhoto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 3,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { PhotosPagingSource(apiService) }
        ).flow
    }

    override fun getPopularVideosFlow(): Flow<PagingData<PexelVideo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 3,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { VideosPagingSource(apiService) }
        ).flow
    }

    override fun searchPhotosFlow(query: String): Flow<PagingData<PexelsPhoto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 3,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { PhotosPagingSource(apiService, query) }
        ).flow
    }

    override fun searchVideosFlow(query: String): Flow<PagingData<PexelVideo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 3,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { VideosPagingSource(apiService, query) }
        ).flow
    }

    override suspend fun getPhotoById(photoId: String): PexelsPhoto {
        return apiService.getPhotoById(photoId)
    }

    override suspend fun getVideoById(videoId: String): PexelVideo {
        return apiService.getVideoById(videoId)
    }

}