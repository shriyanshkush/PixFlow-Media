package com.example.pexel.data.remote.models.photo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PexelPhotoResponse(
    @SerialName("total_results") val totalResults: Int,
    val page: Int,
    @SerialName("per_page") val perPage: Int,
    val photos: List<PexelsPhoto>,
    @SerialName("next_page") val nextPage: String? = null
)