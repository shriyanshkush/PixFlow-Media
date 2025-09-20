package com.example.pexel.data.local.dao

import androidx.room.*
import com.example.pexel.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY dateAdded DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE type = :type ORDER BY dateAdded DESC")
    fun getFavoritesByType(type: String): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE id = :id LIMIT 1")
    suspend fun getFavoriteById(id: String): FavoriteEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)

    @Query("UPDATE favorites SET isDownloaded = :isDownloaded, localPath = :localPath WHERE id = :id")
    suspend fun updateDownloadStatus(id: String, isDownloaded: Boolean, localPath: String?)

    @Query("SELECT COUNT(*) FROM favorites")
    fun getFavoritesCount(): Flow<Int>
}