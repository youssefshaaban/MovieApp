package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.model.MovieEntity
import com.example.data.local.model.PageDataEntity

@Database(entities = [MovieEntity::class, PageDataEntity::class,], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}