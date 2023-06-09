package ru.ilya.moviereviews.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MultimediaDto(

    @SerializedName("height")
    val height: Int? = null,

    @SerializedName("src")
    val src: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("width")
    val width: Int? = null
)