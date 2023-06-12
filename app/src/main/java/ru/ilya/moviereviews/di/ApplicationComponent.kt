package ru.ilya.moviereviews.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.ilya.moviereviews.di.annotations.ApplicationScope
import ru.ilya.moviereviews.presentation.critics_screen.CriticsFragment
import ru.ilya.moviereviews.presentation.reviews_screen.ReviewsFragment

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(fragment: ReviewsFragment)

    fun inject(fragment: CriticsFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}