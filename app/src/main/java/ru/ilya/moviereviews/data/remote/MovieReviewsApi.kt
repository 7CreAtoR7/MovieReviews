package ru.ilya.moviereviews.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ilya.moviereviews.data.remote.dto.ResponseReviewDto

interface MovieReviewsApi {

    @GET("v2/reviews/search.json")
    suspend fun getReviews(
        @Query("type") type: String = "all",
        @Query("api-key") apiKey: String = "VQpX6xSW5uWPA1rAxgXq7a0bKuQjEYQ6",
        @Query("offset") offset: Int = 0
    ): ResponseReviewDto

    companion object {
        private const val BASE_URL = "https://api.nytimes.com/svc/movies/"

        fun create(): MovieReviewsApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(MovieReviewsApi::class.java)
        }
    }
}