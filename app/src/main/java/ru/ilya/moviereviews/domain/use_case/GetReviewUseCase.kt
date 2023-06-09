package ru.ilya.moviereviews.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.ilya.moviereviews.domain.model.Review
import ru.ilya.moviereviews.domain.repository.MovieReviewsRepository
import ru.ilya.moviereviews.util.Resource

class GetReviewUseCase(
    private val movieReviewsRepository: MovieReviewsRepository
) {

    operator fun invoke(type: String, offset: Int): Flow<Resource<List<Review>>> {
        return movieReviewsRepository.getReview(type = type, offset = offset)
    }

}