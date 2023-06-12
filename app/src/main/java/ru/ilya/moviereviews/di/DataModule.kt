package ru.ilya.moviereviews.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ilya.moviereviews.data.local.AppDatabase
import ru.ilya.moviereviews.data.remote.MovieReviewsApi
import ru.ilya.moviereviews.data.repository.MovieReviewsRepositoryImpl
import ru.ilya.moviereviews.di.annotations.ApplicationScope
import ru.ilya.moviereviews.domain.repository.MovieReviewsRepository
import ru.ilya.moviereviews.util.Constants

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindMovieReviewsRepository(impl: MovieReviewsRepositoryImpl): MovieReviewsRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideAppDatabase(
            application: Application
        ): AppDatabase {
            return AppDatabase.getInstance(application)
        }

        @ApplicationScope
        @Provides
        fun provideMovieReviewsApi(): MovieReviewsApi {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(MovieReviewsApi::class.java)

        }
    }
}