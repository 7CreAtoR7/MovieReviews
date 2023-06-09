package ru.ilya.moviereviews.domain.model


data class Review(
    val displayTitle: String,
    val summaryShort: String,
    val byline: String,
    val publicationDate: String,
    val reviewUrl: String,
    val pictureUrl: String,
)
