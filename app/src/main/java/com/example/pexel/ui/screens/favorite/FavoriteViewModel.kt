package com.example.pexel.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexel.data.local.entities.FavoriteEntity
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.data.remote.models.video.PexelVideo
import com.example.pexel.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow("all")
    val selectedFilter = _selectedFilter.asStateFlow()

    val favorites = combine(
        favoriteRepository.getAllFavorites(),
        _selectedFilter
    ) { allFavorites, filter ->
        when (filter) {
            "photos" -> allFavorites.filter { it.type == "photo" }
            "videos" -> allFavorites.filter { it.type == "video" }
            else -> allFavorites
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val favoritesCount = favoriteRepository.getFavoritesCount().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0
    )

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun removeFavorite(favoriteId: String) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(favoriteId)
        }
    }

    fun isFavorite(id: String): Flow<Boolean> {
        return favoriteRepository.isFavorite(id)
    }

    fun addPhotoToFavorites(photo: PexelsPhoto) {
        viewModelScope.launch {
            favoriteRepository.addPhotoToFavorites(photo)
        }
    }

    fun addVideoToFavorites(video: PexelVideo) {
        viewModelScope.launch {
            favoriteRepository.addVideoToFavorites(video)
        }
    }
}