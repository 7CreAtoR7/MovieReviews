package ru.ilya.moviereviews.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.ilya.moviereviews.domain.model.DateRange
import ru.ilya.moviereviews.domain.model.critic_model.Critic
import ru.ilya.moviereviews.domain.model.review_model.Review
import ru.ilya.moviereviews.util.Resource

interface MovieReviewsRepository {

    suspend fun getReviews(query: String?, dateRange: DateRange?): Flow<PagingData<Review>>

    suspend fun getCritics(): Flow<Resource<List<Critic>>>

    suspend fun getCriticsByName(query: String): Flow<Resource<List<Critic>>>
}