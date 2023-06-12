package ru.ilya.moviereviews.data.remote.dto.critics_dto

import com.google.gson.annotations.SerializedName

data class ResponseCriticsDto(

    @SerializedName("copyright")
    val copyright: String? = null,

    @SerializedName("num_results")
    val num_results: Int? = null,

    @SerializedName("results")
    val results: List<CriticDto>? = null,

    @SerializedName("status")
    val status: String? = null
)