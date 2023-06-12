package ru.ilya.moviereviews.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.ilya.moviereviews.domain.model.critic_model.Critic
import ru.ilya.moviereviews.domain.repository.MovieReviewsRepository
import ru.ilya.moviereviews.util.Resource
import javax.inject.Inject

class GetCriticsByNameUseCase @Inject constructor(
    private val movieReviewsRepository: MovieReviewsRepository
) {

    suspend operator fun invoke(query: String): Flow<Resource<List<Critic>>> {
        return movieReviewsRepository.getCriticsByName(query = query)
    }

}