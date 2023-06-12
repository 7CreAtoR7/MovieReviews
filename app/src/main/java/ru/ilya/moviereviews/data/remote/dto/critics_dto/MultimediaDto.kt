package ru.ilya.moviereviews.data.remote.dto.critics_dto

import com.google.gson.annotations.SerializedName

data class MultimediaDto(

    @SerializedName("credit")
    val resource: CriticResourceDto? = null
)