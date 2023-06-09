package ru.ilya.moviereviews.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.ilya.moviereviews.data.local.dao.ReviewsDao
import ru.ilya.moviereviews.data.local.entity.ReviewEntity

@Database(
    entities = [ReviewEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ReviewsDatabase : RoomDatabase() {

    // абстрактный метод, возвращающий реализацию Dao для SubscribedStreams
    abstract fun reviewsDao(): ReviewsDao

    companion object {

        private var INSTANCE: ReviewsDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "ReviewsApp.db"

        fun getInstance(application: Application): ReviewsDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    ReviewsDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}