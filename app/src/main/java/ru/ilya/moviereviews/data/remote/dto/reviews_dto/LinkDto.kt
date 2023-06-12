package ru.ilya.moviereviews.data.remote.dto.reviews_dto

import com.google.gson.annotations.SerializedName

data class LinkDto(

    @SerializedName("suggested_link_text")
    val suggestedLinkText: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("url")
    val url: String? = null
)
