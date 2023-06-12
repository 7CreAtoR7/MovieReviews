package ru.ilya.moviereviews.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.ilya.moviereviews.data.local.AppDatabase
import ru.ilya.moviereviews.data.local.entity.ReviewEntity
import ru.ilya.moviereviews.data.mapper.AppMapper
import ru.ilya.moviereviews.domain.model.DateRange
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ReviewRemoteMediator @Inject constructor(
    private val api: MovieReviewsApi,
    private val mapper: AppMapper,
    private val reviewsDb: AppDatabase,
    private val query: String?,
    private val dateRange: DateRange?
) : RemoteMediator<Int, ReviewEntity>() {

    private var pageIndex = INIT_PAGE

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> INIT_PAGE
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ReviewEntity>
    ): MediatorResult {

        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize
        val offset = pageIndex * limit

        return try {
            val publicationDay = "${dateRange?.startDate ?: ""}:${dateRange?.endDate ?: ""}"
            val resDay = if (publicationDay == ":") "" else publicationDay

            Log.e("REVIEW_MEDIATOR", "ReviewRemoteMediator запрос api offset = $offset")

            val reviewsDto = api.getReviews(offset = offset, query = query ?: "",
                publicationDate = resDay).results ?: emptyList()

            reviewsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    reviewsDb.reviewsDao().deleteAllReviews()
                }
                val reviewsEntities = mapper.mapListReviewDtoToListReviewEntity(reviewsDto)
                reviewsDb.reviewsDao().insertReviews(reviewsEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = reviewsDto.size < limit
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)

        }
    }

    companion object {

        private const val INIT_PAGE = 0
    }
}