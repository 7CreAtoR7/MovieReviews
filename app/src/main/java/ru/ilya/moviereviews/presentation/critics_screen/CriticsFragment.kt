package ru.ilya.moviereviews.presentation.critics_screen

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.ilya.moviereviews.presentation.MainActivity
import ru.ilya.moviereviews.R
import ru.ilya.moviereviews.databinding.FragmentCriticsBinding
import ru.ilya.moviereviews.domain.model.UIState
import ru.ilya.moviereviews.presentation.ReviewsApplication
import ru.ilya.moviereviews.presentation.ViewModelFactory
import ru.ilya.moviereviews.presentation.critic_info_screen.CriticInfoFragment
import ru.ilya.moviereviews.presentation.critics_screen.adapter.CriticsAdapter
import javax.inject.Inject

class CriticsFragment : Fragment() {

    private lateinit var viewModel: CriticsViewModel
    private val criticsAdapter by lazy { CriticsAdapter() }

    private var _binding: FragmentCriticsBinding? = null
    private val binding: FragmentCriticsBinding
        get() = _binding ?: FragmentCriticsBinding.inflate(layoutInflater).also { _binding = it }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as ReviewsApplication).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        (requireActivity() as MainActivity).bottomNavigation.setBottomNavigationViewVisibility(this)
        _binding = FragmentCriticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[CriticsViewModel::class.java]
        initObservers()
        setupRecyclerView()
        initClickListeners()
        initSearchListener()

        getCritics()
    }

    private fun initSearchListener() {
        binding.searchEditText.addTextChangedListener { text ->
            lifecycleScope.launch {
                text?.let { query ->
                    viewModel.onSearch(query.toString())
                }
            }
        }
    }

    private fun getCritics() {
        viewModel.getCritics()
    }

    private fun setupRecyclerView() {
        binding.criticsRecyclerView.layoutManager = GridLayoutManager(activity, 2)
        binding.criticsRecyclerView.adapter = criticsAdapter

        binding.reviewSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getCritics()
            binding.reviewSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initClickListeners() {
        criticsAdapter.onCriticClickListener = {
            launchCriticInfoFragment(CriticInfoFragment.newInstance(it))
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { event ->
                when (event) {
                    is UIState.Success -> {
                        binding.loader.visibility = View.GONE
                        criticsAdapter.submitList(event.criticsList)
                    }
                    is UIState.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                        if (event.criticsListFromLastSession.isNotEmpty()) {
                            criticsAdapter.submitList(event.criticsListFromLastSession)
                            binding.loader.visibility = View.GONE
                        }
                    }
                    is UIState.Error -> {
                        binding.loader.visibility = View.GONE
                        showSnackBarError(message = event.message)
                    }
                    is UIState.Init -> {}
                }
            }
        }
    }

    private fun showSnackBarError(message: String) {
        val snack = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val view: View = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_VERTICAL
        view.layoutParams = params
        snack.show()
    }

    private fun launchCriticInfoFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}