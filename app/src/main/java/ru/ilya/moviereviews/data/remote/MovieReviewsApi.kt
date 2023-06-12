package ru.ilya.moviereviews.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.ilya.moviereviews.data.remote.dto.critics_dto.ResponseCriticsDto
import ru.ilya.moviereviews.data.remote.dto.reviews_dto.ResponseReviewDto

interface MovieReviewsApi {

    @GET("v2/reviews/search.json")
    suspend fun getReviews(
        @Query(TYPE) type: String = TYPE_ALL,
        @Query(API_KEY) apiKey: String = APIKEY,
        @Query(OFFSET) offset: Int = DEFAULT_OFFSET,
        @Query(QUERY) query: String = DEFAULT_QUERY,
        @Query(PUBLICATION_DATE) publicationDate: String = DEFAULT_PUBLICATION_DATE
    ): ResponseReviewDto

    @GET("v2/critics/all.json")
    suspend fun getCritics(
        @Query(API_KEY) apiKey: String = APIKEY,
    ): ResponseCriticsDto


    companion object {

        const val TYPE = "type"
        const val API_KEY = "api-key"
        const val OFFSET = "offset"
        const val QUERY = "query"
        const val PUBLICATION_DATE = "publication-date"

        const val TYPE_ALL = "all"
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_QUERY = ""
        const val DEFAULT_PUBLICATION_DATE = ""
        const val APIKEY = "GW5a0tJfWOcfQ7k3dpQizIsrmpZ33Bmm"
    }
}