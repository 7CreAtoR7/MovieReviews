package ru.ilya.moviereviews.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.ilya.moviereviews.data.local.dao.ReviewsDao
import ru.ilya.moviereviews.data.mapper.AppMapper
import ru.ilya.moviereviews.data.remote.MovieReviewsApi
import ru.ilya.moviereviews.domain.model.Review
import ru.ilya.moviereviews.domain.repository.MovieReviewsRepository
import ru.ilya.moviereviews.util.Resource
import java.io.IOException

class MovieReviewsRepositoryImpl(
    private val dao: ReviewsDao,
    private val api: MovieReviewsApi,
    private val mapper: AppMapper
): MovieReviewsRepository {

    override fun getReview(type: String, offset: Int): Flow<Resource<List<Review>>> = flow {
        emit(Resource.Loading())

        val reviewsFromDb = mapper.mapListReviewEntityToListReview(dao.getReviews())
        emit(Resource.Loading(data = reviewsFromDb))
        try {
            val remoteMovieReviews = mapper.mapListReviewDtoToListReview(api.getReviews(type = type, offset = offset).results)
            dao.deleteReviewsByTitle(remoteMovieReviews.map { it.displayTitle })
            dao.insertReviewsEntity(remoteMovieReviews.map { mapper.mapReviewToReviewEntity(it) })
        } catch(e: HttpException) {
            emit(Resource.Error(
                message = "Oops, something went wrong!",
                data = reviewsFromDb
            ))
        } catch(e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection.",
                data = reviewsFromDb
            ))
        }

        val newMovieReviews = mapper.mapListReviewEntityToListReview((dao.getReviews()))
        emit(Resource.Success(newMovieReviews))
    }
}