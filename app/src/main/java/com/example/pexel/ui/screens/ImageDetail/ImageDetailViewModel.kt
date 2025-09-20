package com.example.pexel.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexel.data.remote.repository.PexelRepository
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ImageDetailUiState {
    object Loading : ImageDetailUiState()
    data class Success(val photo: PexelsPhoto) : ImageDetailUiState()
    data class Error(val message: String) : ImageDetailUiState()
}

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val repository: PexelRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ImageDetailUiState>(ImageDetailUiState.Loading)
    val uiState: StateFlow<ImageDetailUiState> = _uiState.asStateFlow()

    fun loadPhotoDetails(photoId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = ImageDetailUiState.Loading
                // Note: You'll need to add this method to your repository and API
                val photo = repository.getPhotoById(photoId)
                _uiState.value = ImageDetailUiState.Success(photo)
            } catch (e: Exception) {
                _uiState.value = ImageDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun togglePhotoFavorite(photo: PexelsPhoto) {
        viewModelScope.launch {
            val isFavorite = favoriteRepository.isFavorite(photo.id.toString()).first()
            if (isFavorite) {
                favoriteRepository.removeFavorite(photo.id.toString())
            } else {
                favoriteRepository.addPhotoToFavorites(photo)
            }
        }
    }
}