package ru.ilya.moviereviews.presentation.reviews_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ilya.moviereviews.R
import ru.ilya.moviereviews.databinding.ReviewItemBinding
import ru.ilya.moviereviews.domain.model.review_model.Review
import ru.ilya.moviereviews.util.convertDateFormat

class ReviewsAdapter :
    PagingDataAdapter<Review, ReviewsAdapter.ReviewViewHolder>(ReviewDiffCallback()) {

    var onReadViewButtonClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReviewItemBinding.inflate(inflater, parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewItem = getItem(position) ?: return
        with(holder.binding) {
            reviewTitle.text = reviewItem.displayTitle
            shortReviewDescription.text = reviewItem.summaryShort
            reviewAuthor.text = reviewItem.byline
            reviewPublicationDate.text = reviewItem.publicationDate.convertDateFormat()
            readReviewButton.setOnClickListener {
                onReadViewButtonClickListener?.invoke(reviewItem.reviewUrl)
            }
            loadReviewImage(reviewImage, reviewItem.pictureUrl)
        }
    }

    private fun loadReviewImage(imageView: ImageView, url: String) {
        val context = imageView.context
        if (url.isNotBlank()) {
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_assistant)
                .error(R.drawable.ic_assistant)
                .into(imageView)
        } else {
            Glide.with(context)
                .load(R.drawable.ic_assistant)
                .into(imageView)
        }
    }

    class ReviewViewHolder(
        val binding: ReviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

}