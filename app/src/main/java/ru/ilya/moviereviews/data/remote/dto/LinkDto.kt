package ru.ilya.moviereviews.data.remote.dto

import com.google.gson.annotations.SerializedName
import ru.ilya.moviereviews.domain.model.Link

data class LinkDto(

    @SerializedName("suggested_link_text")
    val suggestedLinkText: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("url")
    val url: String? = null
)
