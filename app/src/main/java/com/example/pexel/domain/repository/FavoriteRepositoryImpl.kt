package com.example.pexel.data.repository

import com.example.pexel.data.local.dao.FavoriteDao
import com.example.pexel.data.local.entities.FavoriteEntity
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.data.remote.models.video.PexelVideo
import com.example.pexel.domain.repository.FavoriteRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val gson: Gson
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorites()
    }

    override fun getFavoritesByType(type: String): Flow<List<FavoriteEntity>> {
        return favoriteDao.getFavoritesByType(type)
    }

    override fun isFavorite(id: String): Flow<Boolean> {
        return favoriteDao.isFavorite(id)
    }

    override suspend fun addPhotoToFavorites(photo: PexelsPhoto) {
        val favoriteEntity = FavoriteEntity(
            id = photo.id.toString(),
            type = "photo",
            title = photo.alt ?: "Untitled Photo",
            creator = photo.photographer,
            imageUrl = photo.src.medium,
            originalUrl = photo.src.original,
            width = photo.width,
            height = photo.height,
            avgColor = photo.avgColor
        )
        favoriteDao.insertFavorite(favoriteEntity)
    }

    override suspend fun addVideoToFavorites(video: PexelVideo) {
        val videoUrlsJson = gson.toJson(video.videoFiles)
        val favoriteEntity = FavoriteEntity(
            id = video.id.toString(),
            type = "video",
            title = "Video by ${video.user?.name ?: "Unknown"}",
            creator = video.user?.name ?: "Unknown Creator",
            imageUrl = video.image,
            originalUrl = video.videoFiles?.firstOrNull { it.quality == "hd" }?.link
                ?: video.videoFiles?.firstOrNull()?.link ?: "",
            width = video.width,
            height = video.height,
            duration = video.duration,
            videoUrls = videoUrlsJson
        )
        favoriteDao.insertFavorite(favoriteEntity)
    }

    override suspend fun removeFavorite(id: String) {
        favoriteDao.deleteFavoriteById(id)
    }

    override suspend fun updateDownloadStatus(id: String, isDownloaded: Boolean, localPath: String?) {
        favoriteDao.updateDownloadStatus(id, isDownloaded, localPath)
    }

    override fun getFavoritesCount(): Flow<Int> {
        return favoriteDao.getFavoritesCount()
    }
}