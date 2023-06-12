package ru.ilya.moviereviews.data.remote.dto.critics_dto

import com.google.gson.annotations.SerializedName

data class CriticResourceDto(

    @SerializedName("credit")
    val credit: String? = null,

    @SerializedName("height")
    val height: Int? = null,

    @SerializedName("src")
    val src: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("width")
    val width: Int? = null
)