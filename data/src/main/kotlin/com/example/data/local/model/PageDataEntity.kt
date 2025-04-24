package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pages")
data class PageDataEntity(
    @PrimaryKey val page: Int,
    val totalPages: Int,
    val totalResults: Int
)
