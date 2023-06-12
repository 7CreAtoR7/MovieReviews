package ru.ilya.moviereviews.data.remote.dto.critics_dto

import com.google.gson.annotations.SerializedName

data class CriticDto(

    @SerializedName("bio")
    val bio: String? = null,

    @SerializedName("display_name")
    val displayName: String? = null,

    @SerializedName("multimedia")
    val multimedia: MultimediaDto? = null,

    @SerializedName("seo_name")
    val seoName: String? = null,

    @SerializedName("sort_name")
    val sortName: String? = null,

    @SerializedName("status")
    val status: String? = null
)