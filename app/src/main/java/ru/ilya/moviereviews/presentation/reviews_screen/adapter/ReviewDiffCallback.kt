package ru.ilya.moviereviews.presentation.reviews_screen.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.ilya.moviereviews.domain.model.review_model.Review

class ReviewDiffCallback: DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.displayTitle == newItem.displayTitle
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}
