package com.example.pexel.presentation.viewmodel

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.pexel.data.remote.repository.PexelRepository
import com.example.pexel.data.remote.models.video.PexelVideo
import com.example.pexel.data.repository.FavoriteRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class VideoDetailUiState {
    object Loading : VideoDetailUiState()
    data class Success(val video: PexelVideo) : VideoDetailUiState()
    data class Error(val message: String) : VideoDetailUiState()
}

@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    private val repository: PexelRepository,
    private val favoriteRepositoryImpl: FavoriteRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<VideoDetailUiState>(VideoDetailUiState.Loading)
    val uiState: StateFlow<VideoDetailUiState> = _uiState.asStateFlow()

    // Add this to your VideoDetailViewModel's loadVideoDetails method
    @OptIn(UnstableApi::class)
    fun loadVideoDetails(videoId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = VideoDetailUiState.Loading

                // Add this temporary test
                Log.d("VideoDetailViewModel", "About to call repository.getVideoById($videoId)")

                val video = repository.getVideoById(videoId)

                // Detailed logging
                Log.d("VideoDetailViewModel", "Received video: ${video.id}")
                Log.d("VideoDetailViewModel", "Video.videoFiles == null: ${video.videoFiles == null}")
                if (video.videoFiles != null) {
                    Log.d("VideoDetailViewModel", "Video.videoFiles.size: ${video.videoFiles!!.size}")
                    video.videoFiles!!.forEachIndexed { index, file ->
                        Log.d("VideoDetailViewModel", "VideoFile[$index]: ${file.quality} - ${file.link}")
                    }
                }

                _uiState.value = VideoDetailUiState.Success(video)
            } catch (e: Exception) {
                Log.e("VideoDetailViewModel", "Error loading video: ${e.message}", e)
                _uiState.value = VideoDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleVideoFavorite(video: PexelVideo) {
        viewModelScope.launch {
            val isFavorite = favoriteRepositoryImpl.isFavorite(video.id.toString()).first()
            if (isFavorite) {
                favoriteRepositoryImpl.removeFavorite(video.id.toString())
            } else {
                favoriteRepositoryImpl.addVideoToFavorites(video)
            }
        }
    }
}