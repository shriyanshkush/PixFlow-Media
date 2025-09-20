package com.example.pexel.ui.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object Home : Routes()

    @Serializable
    data object Favourite : Routes()

    @Serializable
    data object Downloads : Routes()

    @Serializable
    data class ImageDetail(val photoId: String) : Routes()

    @Serializable
    data class VideoDetail(val videoId: String) : Routes()

}