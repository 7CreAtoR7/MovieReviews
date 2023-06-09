package ru.ilya.moviereviews.presentation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ilya.moviereviews.data.local.ReviewsDatabase
import ru.ilya.moviereviews.data.mapper.AppMapper
import ru.ilya.moviereviews.data.remote.MovieReviewsApi
import ru.ilya.moviereviews.data.repository.MovieReviewsRepositoryImpl
import ru.ilya.moviereviews.domain.repository.MovieReviewsRepository
import ru.ilya.moviereviews.domain.use_case.GetReviewUseCase
import ru.ilya.moviereviews.util.Resource

class MovieReviewsViewModel(context: Application) : AndroidViewModel(context) {

    private val getReviewUseCase: GetReviewUseCase = GetReviewUseCase(MovieReviewsRepositoryImpl(
        ReviewsDatabase.getInstance(context).reviewsDao(), MovieReviewsApi.create(), AppMapper()
    ))

//    private val _state = mutableStateOf(WordInfoState())
//    val state: State<WordInfoState> = _state
//
//    private val _eventFlow = MutableSharedFlow<UIEvent>()
//    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun onSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            getReviewUseCase.invoke(type = "all", offset = 0)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            Log.e("CHECKMY", "SUCCESS: ${result.data}")
//                            _state.value = state.value.copy(
//                                wordInfoItems = result.data ?: emptyList(),
//                                isLoading = false
//                            )
                        }
                        is Resource.Error -> {
                            Log.e("CHECKMY", "ERROR: ${result.data}")
//                            _state.value = state.value.copy(
//                                wordInfoItems = result.data ?: emptyList(),
//                                isLoading = false
//                            ) _eventFlow . emit (UIEvent.ShowSnackbar(
//                                result.message ?: "Unknown error"
//                            ))
                        }
                        is Resource.Loading -> {
                            Log.e("CHECKMY", "LOADING: ${result.data}")
//                            _state.value = state.value.copy(
//                                wordInfoItems = result.data ?: emptyList(),
//                                isLoading = true
//                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}
