package ru.ilya.moviereviews.domain.model.critic_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Critic(
    val bio: String,
    val displayName: String,
    val status: String,
    val photoUrl: String
) : Parcelable