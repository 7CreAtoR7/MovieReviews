package ru.ilya.moviereviews.presentation.reviews_screen

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import ru.ilya.moviereviews.databinding.FragmentReviewsBinding
import ru.ilya.moviereviews.domain.model.DateRange
import ru.ilya.moviereviews.presentation.MainActivity
import ru.ilya.moviereviews.presentation.ReviewsApplication
import ru.ilya.moviereviews.presentation.ViewModelFactory
import ru.ilya.moviereviews.presentation.reviews_screen.adapter.DefaultLoadStateAdapter
import ru.ilya.moviereviews.presentation.reviews_screen.adapter.ReviewsAdapter
import ru.ilya.moviereviews.presentation.reviews_screen.adapter.TryAgainAction
import java.io.IOException
import java.util.*
import javax.inject.Inject

class ReviewsFragment : Fragment() {

    private lateinit var viewModel: MovieReviewsViewModel
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder
    private val reviewsAdapter by lazy { ReviewsAdapter() }

    private var _binding: FragmentReviewsBinding? = null
    private val binding: FragmentReviewsBinding
        get() = _binding ?: FragmentReviewsBinding.inflate(layoutInflater).also { _binding = it }

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
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieReviewsViewModel::class.java]

        setupRecyclerView()
        initObservers()
        initClickListeners()
        observeLoadState()
        setupSwipeToRefresh()
        setupSearchInput()
    }


    private fun setupRecyclerView() {
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(activity)

        val tryAgainAction: TryAgainAction = { reviewsAdapter.retry() }
        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)

        // адаптер с кнопкой "попробовать еще раз" под основным адаптером
        val adapterWithLoadState = reviewsAdapter.withLoadStateFooter(footerAdapter)

        binding.reviewsRecyclerView.adapter = adapterWithLoadState
        (binding.reviewsRecyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations =
            false

        mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
            binding.loadStateView,
            binding.reviewSwipeRefreshLayout,
            tryAgainAction
        )

        handleScrollingToTopWhenSearching(reviewsAdapter)
    }


    private fun setupSwipeToRefresh() {
        binding.reviewSwipeRefreshLayout.setOnRefreshListener {
            reviewsAdapter.refresh()
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviewsFlow.collect { pagingData ->
                reviewsAdapter.submitData(lifecycle, pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviewsFlowFilter.collect { pagingData ->
                reviewsAdapter.submitData(lifecycle, pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            reviewsAdapter.loadStateFlow.collectLatest { loadState ->
                // Обработка состояния загрузки данных
                when (loadState.refresh) {
                    is LoadState.Loading -> {}
                    is LoadState.Error -> {
                        val errorInfo = when ((loadState.refresh as LoadState.Error).error) {
                            is IOException -> "Нет интернет соединения."
                            is HttpException -> "Слишком много запросов, повторите позже."
                            else -> "Что-то пошло не так, повторите позже."
                        }
                        Toast.makeText(requireActivity(), errorInfo, Toast.LENGTH_SHORT).show()
                    }
                    is LoadState.NotLoading -> {}
                }
            }
        }
    }

    private fun initClickListeners() {
        reviewsAdapter.onReadViewButtonClickListener = { reviewUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(reviewUrl))
            startActivity(intent)
        }
        binding.filterButton.setOnClickListener {
            showDatePickerDialog()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleScrollingToTopWhenSearching(adapter: ReviewsAdapter) =
        viewLifecycleOwner.lifecycleScope.launch {
            getRefreshLoadStateFlow(adapter)
                .simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.reviewsRecyclerView.scrollToPosition(0)
                    }
                }
        }

    private fun getRefreshLoadStateFlow(adapter: ReviewsAdapter): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }

    private fun setupSearchInput() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.query = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    @OptIn(FlowPreview::class)
    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            reviewsAdapter.loadStateFlow.debounce(200).collectLatest { state ->
                mainLoadStateHolder.bind(state.refresh)
            }
        }
    }

    // Открытие диалогового окна DatePickerDialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->

                // Добавляем ведущий ноль, если месяц меньше 10
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay)
                val startDate = "$selectedYear-${formattedMonth}-$formattedDay"

                // Создаем диалоговое окно с выбором второй даты
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, selectedFinishYear, selectedFinishMonth, selectedFinishDay ->

                        val formattedMonthEnd = String.format("%02d", selectedFinishMonth + 1)
                        val formattedDayEnd = String.format("%02d", selectedFinishDay)
                        val endDate = "$selectedFinishYear-${formattedMonthEnd}-$formattedDayEnd"

                        handleDateRangeSelected(startDate, endDate)
                    },
                    year,
                    month,
                    day
                )

                datePickerDialog.show()
            }, year, month, day)
        datePickerDialog.setTitle("Выберите диапазон даты публикации")
        datePickerDialog.show()
    }

    private fun handleDateRangeSelected(startDate: String, endDate: String) {
        viewModel.dateRange = DateRange(startDate = startDate, endDate = endDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@ExperimentalCoroutinesApi
fun <T> Flow<T>.simpleScan(count: Int): Flow<List<T?>> {
    val items = List<T?>(count) { null }
    return this.scan(items) { previous, value -> previous.drop(1) + value }
}