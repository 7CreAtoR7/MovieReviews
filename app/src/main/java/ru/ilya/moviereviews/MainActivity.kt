package ru.ilya.moviereviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Job
import ru.ilya.moviereviews.data.mapper.AppMapper
import ru.ilya.moviereviews.data.remote.MovieReviewsApi
import ru.ilya.moviereviews.data.repository.MovieReviewsRepositoryImpl
import ru.ilya.moviereviews.presentation.MovieReviewsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieReviewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MovieReviewsViewModel::class.java]
        viewModel.onSearch()


    }
}