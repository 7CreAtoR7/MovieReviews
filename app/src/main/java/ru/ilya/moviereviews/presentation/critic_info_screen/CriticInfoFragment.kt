package ru.ilya.moviereviews.presentation.critic_info_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.ilya.moviereviews.presentation.MainActivity
import ru.ilya.moviereviews.R
import ru.ilya.moviereviews.databinding.FragmentCriticInfoBinding
import ru.ilya.moviereviews.domain.model.critic_model.Critic

class CriticInfoFragment : Fragment() {

    private lateinit var criticInfo: Critic

    private var _binding: FragmentCriticInfoBinding? = null
    private val binding: FragmentCriticInfoBinding
        get() = _binding ?: FragmentCriticInfoBinding.inflate(layoutInflater).also { _binding = it }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        (requireActivity() as MainActivity).bottomNavigation.setBottomNavigationViewVisibility(this)
        _binding = FragmentCriticInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
        initClickListeners()

        with(binding) {
            if (criticInfo.photoUrl.isNotBlank()) {
                loadReviewImage(mainPhoto, criticInfo.photoUrl)
                loadReviewImage(profile, criticInfo.photoUrl)
            }
            collapsingToolbar.title = criticInfo.displayName
            if (criticInfo.status.isNotBlank())
                status.text = criticInfo.status
            if (criticInfo.bio.isNotBlank())
                bio.text = criticInfo.bio
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

    private fun initClickListeners() {
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(CRITIC_INFO)) {
            throw RuntimeException("Param CRITIC_INFO is absent!")
        }
        criticInfo = args.getParcelable(CRITIC_INFO) ?: Critic(
            bio = EMPTY_LINE,
            displayName = EMPTY_LINE,
            status = EMPTY_LINE,
            photoUrl = EMPTY_LINE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val CRITIC_INFO = "critic_info"
        private const val EMPTY_LINE = ""

        fun newInstance(critic: Critic): CriticInfoFragment {
            return CriticInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CRITIC_INFO, critic)
                }
            }
        }
    }
}