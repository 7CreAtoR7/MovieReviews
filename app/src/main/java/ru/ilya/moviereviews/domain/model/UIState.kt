package ru.ilya.moviereviews.domain.model

import ru.ilya.moviereviews.domain.model.critic_model.Critic

sealed class UIState {
    object Init : UIState()
    data class Loading(val criticsListFromLastSession: List<Critic>) : UIState()
    data class Success(val criticsList: List<Critic>) : UIState()
    data class Error(val message: String) : UIState()
}