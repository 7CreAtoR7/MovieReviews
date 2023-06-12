package ru.ilya.moviereviews.domain.use_case

import android.util.Log
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.ilya.moviereviews.domain.model.DateRange
import ru.ilya.moviereviews.domain.model.review_model.Review
import ru.ilya.moviereviews.domain.repository.MovieReviewsRepository
import javax.inject.Inject

class GetReviewUseCase @Inject constructor(
    private val movieReviewsRepository: MovieReviewsRepository
) {

    suspend operator fun invoke(query: String?, dateRange: DateRange?): Flow<PagingData<Review>> {
        return movieReviewsRepository.getReviews(query = query, dateRange = dateRange)
    }

}