package ru.ilya.moviereviews.data.remote.dto.reviews_dto

import com.google.gson.annotations.SerializedName

data class ResponseReviewDto(

    @SerializedName("copyright")
    val copyright: String? = null,

    @SerializedName("has_more")
    val hasMore: Boolean? = null,

    @SerializedName("num_results")
    val numResults: Int? = null,

    @SerializedName("results")
    val results: List<ReviewDto>? = null,

    @SerializedName("status")
    val status: String? = null
)