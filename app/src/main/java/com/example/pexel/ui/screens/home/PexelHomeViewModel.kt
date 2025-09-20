package com.example.pexel.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pexel.data.remote.repository.PexelRepository
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.data.remote.models.video.PexelVideo
import com.example.pexel.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PexelHomeViewModel @Inject constructor(
    private val repository: PexelRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Photos paging flow
    val photos: Flow<PagingData<PexelsPhoto>> = combine(
        _searchQuery,
        _selectedTab
    ) { query, tab ->
        if (tab == 0) { // Photos tab
            if (query.isBlank()) {
                repository.getPopularPhotosFlow()
            } else {
                repository.searchPhotosFlow(query)
            }
        } else {
            flowOf(PagingData.empty())
        }
    }.flatMapLatest { it }
        .cachedIn(viewModelScope)

    // Videos paging flow
    val videos: Flow<PagingData<PexelVideo>> = combine(
        _searchQuery,
        _selectedTab
    ) { query, tab ->
        if (tab == 1) { // Videos tab
            if (query.isBlank()) {
                repository.getPopularVideosFlow()
            } else {
                repository.searchVideosFlow(query)
            }
        } else {
            flowOf(PagingData.empty())
        }
    }.flatMapLatest { it }
        .cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun clearSearch() {
        _searchQuery.value = ""
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

    fun toggleVideoFavorite(video: PexelVideo) {
        viewModelScope.launch {
            val isFavorite = favoriteRepository.isFavorite(video.id.toString()).first()
            if (isFavorite) {
                favoriteRepository.removeFavorite(video.id.toString())
            } else {
                favoriteRepository.addVideoToFavorites(video)
            }
        }
    }
}