package ru.ilya.moviereviews.presentation.critics_screen.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.ilya.moviereviews.domain.model.critic_model.Critic

class CriticDiffCallback : DiffUtil.ItemCallback<Critic>() {
    override fun areItemsTheSame(oldItem: Critic, newItem: Critic): Boolean {
        return oldItem.photoUrl == newItem.photoUrl
    }

    override fun areContentsTheSame(oldItem: Critic, newItem: Critic): Boolean {
        return oldItem == newItem
    }
}
