package ru.ilya.moviereviews.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.ilya.moviereviews.data.local.dao.CriticsDao
import ru.ilya.moviereviews.data.local.dao.ReviewsDao
import ru.ilya.moviereviews.data.local.entity.CriticEntity
import ru.ilya.moviereviews.data.local.entity.ReviewEntity

@Database(
    entities = [ReviewEntity::class, CriticEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reviewsDao(): ReviewsDao

    abstract fun criticsDao(): CriticsDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "Critics_reviews.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
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