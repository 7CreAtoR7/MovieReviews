package ru.ilya.moviereviews.data.remote.dto.reviews_dto

import com.google.gson.annotations.SerializedName

data class ReviewDto(

    @SerializedName("byline")
    val byline: String? = null,

    @SerializedName("critics_pick")
    val criticsPick: Int? = null,

    @SerializedName("date_updated")
    val dateUpdated: String? = null,

    @SerializedName("display_title")
    val displayTitle: String? = null,

    @SerializedName("headline")
    val headline: String? = null,

    @SerializedName("link")
    val link: LinkDto,

    @SerializedName("mpaa_rating")
    val mpaaRating: String? = null,

    @SerializedName("multimedia")
    val multimedia: MultimediaDto? = null,

    @SerializedName("opening_date")
    val openingDate: String? = null,

    @SerializedName("publication_date")
    val publicationDate: String? = null,

    @SerializedName("summary_short")
    val summaryShort: String? = null
)