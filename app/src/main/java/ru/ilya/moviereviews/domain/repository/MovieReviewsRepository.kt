package ru.ilya.moviereviews.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ilya.moviereviews.domain.model.Review
import ru.ilya.moviereviews.util.Resource

interface MovieReviewsRepository {

    fun getReview(type: String, offset: Int): Flow<Resource<List<Review>>>
}