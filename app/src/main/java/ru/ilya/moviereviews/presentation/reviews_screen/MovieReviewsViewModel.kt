package ru.ilya.moviereviews.presentation.reviews_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import ru.ilya.moviereviews.domain.model.DateRange
import ru.ilya.moviereviews.domain.use_case.GetReviewUseCase
import javax.inject.Inject

class MovieReviewsViewModel @Inject constructor(
    private val getReviewUseCase: GetReviewUseCase
) : ViewModel() {

    private val queryLiveData = MutableLiveData("")

    var query: String?
        get() = queryLiveData.value
        set(value) {
            queryLiveData.value = value
        }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val reviewsFlow = queryLiveData.asFlow()
        .distinctUntilChanged()
        .debounce(500)
        .flatMapLatest {
            getReviewUseCase(query = it, null)
        }
        .cachedIn(viewModelScope)



    private val dateRangeLiveData = MutableLiveData(DateRange("", ""))

    var dateRange: DateRange?
        get() = dateRangeLiveData.value
        set(value) {
            dateRangeLiveData.value = value
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val reviewsFlowFilter = dateRangeLiveData.asFlow()
        .distinctUntilChanged()
        .filter { it.endDate.isNotBlank() && it.startDate.isNotBlank() }
        .flatMapLatest {
            getReviewUseCase(query = null, it)
        }
        .cachedIn(viewModelScope)
}
