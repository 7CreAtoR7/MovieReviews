package ru.ilya.moviereviews.presentation.reviews_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.ilya.moviereviews.databinding.FragmentReviewsBinding

class ReviewsFragment: Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding: FragmentReviewsBinding
        get() = _binding ?: throw RuntimeException("FragmentReviewsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}