package ru.ilya.moviereviews.presentation.critics_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.ilya.moviereviews.domain.model.UIState
import ru.ilya.moviereviews.domain.use_case.GetCriticsByNameUseCase
import ru.ilya.moviereviews.domain.use_case.GetCriticsUseCase
import ru.ilya.moviereviews.util.Resource
import javax.inject.Inject

class CriticsViewModel @Inject constructor(
    private val getCriticsUseCase: GetCriticsUseCase,
    private val getCriticsByNameUseCase: GetCriticsByNameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UIState>(UIState.Init)
    val state = _state.asSharedFlow()

    private var searchJob: Job? = null

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun onSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            flow { emit(query) }
                .debounce(500) // Добавляем задержку 500 мс
                .flatMapLatest { updatedQuery ->
                    getCriticsByNameUseCase(updatedQuery)
                }
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = UIState.Success(
                                criticsList = result.data ?: emptyList()
                            )
                        }
                        is Resource.Error -> {
                            _state.emit(
                                UIState.Error(
                                    message = result.message
                                        ?: "Что-то пошло не так, повторите позже."
                                )
                            )
                        }
                        is Resource.Loading -> {
                            _state.emit(
                                UIState.Loading(
                                    criticsListFromLastSession = result.data ?: emptyList()
                                )
                            )
                        }
                    }
                }
        }
    }

    fun getCritics() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            getCriticsUseCase()
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = UIState.Success(
                                criticsList = result.data ?: emptyList()
                            )
                        }
                        is Resource.Error -> {
                            Log.e("REVIEW_MEDIATOR", "отправка ошибки ${result.message}")
                            _state.emit(
                                UIState.Error(
                                    message = result.message
                                        ?: "Что-то пошло не так, повторите позже."
                                )
                            )
                        }
                        is Resource.Loading -> {
                            _state.emit(
                                UIState.Loading(
                                    criticsListFromLastSession = result.data ?: emptyList()
                                )
                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}