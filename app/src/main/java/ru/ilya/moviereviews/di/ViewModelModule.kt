package ru.ilya.moviereviews.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ilya.moviereviews.di.annotations.ViewModelKey
import ru.ilya.moviereviews.presentation.critics_screen.CriticsViewModel
import ru.ilya.moviereviews.presentation.reviews_screen.MovieReviewsViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieReviewsViewModel::class)
    fun bindMovieReviewsViewModel(viewModel: MovieReviewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CriticsViewModel::class)
    fun bindCriticsViewModel(viewModel: CriticsViewModel): ViewModel
}