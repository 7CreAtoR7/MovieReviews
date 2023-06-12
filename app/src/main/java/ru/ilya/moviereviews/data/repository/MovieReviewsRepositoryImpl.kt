package ru.ilya.moviereviews.data.repository

import android.util.Log
import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.ilya.moviereviews.data.local.AppDatabase
import ru.ilya.moviereviews.data.mapper.AppMapper
import ru.ilya.moviereviews.data.remote.MovieReviewsApi
import ru.ilya.moviereviews.data.remote.ReviewRemoteMediator
import ru.ilya.moviereviews.domain.model.DateRange
import ru.ilya.moviereviews.domain.model.critic_model.Critic
import ru.ilya.moviereviews.domain.model.review_model.Review
import ru.ilya.moviereviews.domain.repository.MovieReviewsRepository
import ru.ilya.moviereviews.util.Resource
import java.io.IOException
import javax.inject.Inject


class MovieReviewsRepositoryImpl @Inject constructor(
    private val appDb: AppDatabase,
    private val api: MovieReviewsApi,
    private val mapper: AppMapper
) : MovieReviewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getReviews(
        query: String?,
        dateRange: DateRange?
    ): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 1,
                prefetchDistance = 0
            ),
            remoteMediator = ReviewRemoteMediator(
                api = api,
                mapper = mapper,
                reviewsDb = appDb,
                query = query,
                dateRange = dateRange
            ),
            pagingSourceFactory = {
                appDb.reviewsDao().getReviews(
                    query = query,
                    startDateRange = dateRange?.startDate,
                    endDateRange = dateRange?.endDate
                )
            }
        )
            .flow
            .map { pagingData ->
                pagingData.map { mapper.mapReviewEntityToReview(it) }
            }
    }

    override suspend fun getCritics(): Flow<Resource<List<Critic>>> = withContext(Dispatchers.IO) {
        flow {
            emit(Resource.Loading())

            val critics = mapper.mapListCriticEntityToListCritic(appDb.criticsDao().getCritics())
            emit(Resource.Loading(data = critics))

            try {
                Log.e("REVIEW_MEDIATOR", "MovieReviewsRepositoryImpl запрос api.getCritics()")
                val remoteCritics = api.getCritics().results ?: emptyList()
                appDb.criticsDao().deleteAllCritics()
                val dbCritics = mapper.mapListCriticDtoToListCriticEntity(remoteCritics)
                appDb.criticsDao().insertCritics(dbCritics)
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = "Нет интернет соединения."
                    )
                )
            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        message = "Что-то пошло не так , повторите позже.",
                    )
                )
            }

            val newCritics = mapper.mapListCriticEntityToListCritic(appDb.criticsDao().getCritics())
            emit(Resource.Success(newCritics))
        }
    }

    override suspend fun getCriticsByName(query: String): Flow<Resource<List<Critic>>> =
        withContext(Dispatchers.IO) {
            flow {
                emit(Resource.Loading())

                val critics = mapper.mapListCriticEntityToListCritic(
                    appDb.criticsDao().getCriticsByName(criticName = query)
                )
                emit(Resource.Loading(data = critics))

                try {
                    val remoteCritics = api.getCritics().results
                        ?.filter {
                            it.displayName?.lowercase()?.contains(query) ?: false
                        } ?: emptyList()
                    appDb.criticsDao().deleteAllCritics()
                    val dbCritics = mapper.mapListCriticDtoToListCriticEntity(remoteCritics)
                    appDb.criticsDao().insertCritics(dbCritics)
                } catch (e: IOException) {
                    emit(
                        Resource.Error(
                            message = "Нет интернет соединения."
                        )
                    )
                } catch (e: HttpException) {
                    emit(
                        Resource.Error(
                            message = "Что-то пошло не так , повторите позже.",
                        )
                    )
                }

                val newCritics = mapper.mapListCriticEntityToListCritic(
                    appDb.criticsDao().getCriticsByName(criticName = query)
                )
                emit(Resource.Success(newCritics))
            }
        }
}