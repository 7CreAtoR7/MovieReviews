package ru.ilya.moviereviews.presentation.critics_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.ilya.moviereviews.R
import ru.ilya.moviereviews.databinding.CriticItemBinding
import ru.ilya.moviereviews.domain.model.critic_model.Critic

class CriticsAdapter : ListAdapter<Critic, CriticsAdapter.CriticViewHolder>(CriticDiffCallback()) {

    var onCriticClickListener: ((Critic) -> Unit)? = null

    class CriticViewHolder(
        val binding: CriticItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriticViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CriticItemBinding.inflate(inflater, parent, false)
        return CriticViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CriticViewHolder, position: Int) {
        val criticItem = getItem(position)
        // открываем фрагмент с детальной информацией о критике
        holder.itemView.setOnClickListener {
            onCriticClickListener?.invoke(criticItem)
        }
        with(holder.binding) {
            criticName.text = criticItem.displayName
            loadReviewImage(criticPhoto, criticItem.photoUrl)
        }
    }

    private fun loadReviewImage(imageView: ImageView, url: String) {
        val context = imageView.context
        if (url.isNotBlank()) {
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.rounded_corners)

            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .apply(requestOptions)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        } else {
            Glide.with(context)
                .load(R.drawable.ic_person)
                .into(imageView)
        }
    }
}