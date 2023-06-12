package ru.ilya.moviereviews.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "criticsTable")
class CriticEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "display_name")
    val displayName: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "bio")
    val bio: String,

    @ColumnInfo(name = "critic_photo")
    val criticPhoto: String
)