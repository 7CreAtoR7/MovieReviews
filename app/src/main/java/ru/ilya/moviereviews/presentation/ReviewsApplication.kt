package ru.ilya.moviereviews.presentation

import android.app.Application
import ru.ilya.moviereviews.di.DaggerApplicationComponent

class ReviewsApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}