package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val original_title: String? = null,
    val overview: String? = null,
    val backdrop: String? = null,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val pageId: Int // Foreign key reference to PageDataEntity
)

