package com.example.pexel.data.remote.models.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PexelVideoResponse(
    val page: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("total_results") val totalResults: Int,
    val url: String,
    val videos: List<PexelVideo>,
    @SerialName("next_page") val nextPage: String? = null,
    @SerialName("prev_page") val prevPage: String? = null
)
