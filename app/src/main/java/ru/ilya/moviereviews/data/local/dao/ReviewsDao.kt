package ru.ilya.moviereviews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ilya.moviereviews.data.local.entity.ReviewEntity


@Dao
interface ReviewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviewsEntity(reviewsEntity: List<ReviewEntity>)

    @Query("DELETE FROM Reviews WHERE display_title IN(:titles)")
    suspend fun deleteReviewsByTitle(titles: List<String>)

    @Query("SELECT * FROM Reviews")
    suspend fun getReviews(): List<ReviewEntity>

    @Query("SELECT * FROM Reviews WHERE display_title LIKE '%' || :title || '%'")
    suspend fun getReviewByTitle(title: String): List<ReviewEntity>

}