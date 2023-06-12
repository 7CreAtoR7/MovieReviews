package ru.ilya.moviereviews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ilya.moviereviews.data.local.entity.CriticEntity

@Dao
interface CriticsDao {

    @Query("SELECT * FROM criticsTable")
    fun getCritics(): List<CriticEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCritics(critics: List<CriticEntity>)

    @Query("DELETE FROM criticsTable")
    suspend fun deleteAllCritics()

    @Query("SELECT * FROM criticsTable WHERE display_name LIKE '%' || :criticName || '%'")
    fun getCriticsByName(criticName: String): List<CriticEntity>
}