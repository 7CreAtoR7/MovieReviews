package ru.ilya.moviereviews.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ilya.moviereviews.data.local.entity.ReviewEntity

@Dao
interface ReviewsDao {

    @Query("SELECT * FROM reviewsTable WHERE " +
            "(:query IS NULL OR display_title LIKE '%' || :query || '%') " +
            "AND (:startDateRange IS NULL OR :endDateRange IS NULL OR DATE(publication_date) " +
            "BETWEEN DATE(:startDateRange) AND DATE(:endDateRange))")
    fun getReviews(query: String?, startDateRange: String?, endDateRange: String?): PagingSource<Int, ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    @Query("DELETE FROM reviewsTable")
    suspend fun deleteAllReviews()
}