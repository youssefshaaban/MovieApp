package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.model.MovieEntity
import com.example.data.local.model.PageDataEntity
import com.example.data.local.model.PageWithMovies

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    suspend fun getAll(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: PageDataEntity)

    @Transaction
    @Query("SELECT * FROM pages WHERE page = :page")
    suspend fun getPageWithMovies(page: Int): PageWithMovies?
}