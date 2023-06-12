package ru.ilya.moviereviews.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviewsTable")
data class ReviewEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "display_title")
    val displayTitle: String,

    @ColumnInfo(name = "summary_short")
    val summaryShort: String,

    @ColumnInfo(name = "byline")
    val byline: String,

    @ColumnInfo(name = "publication_date")
    val publicationDate: String,

    @ColumnInfo(name = "review_url")
    val reviewUrl: String,

    @ColumnInfo(name = "picture_url")
    val pictureUrl: String
)