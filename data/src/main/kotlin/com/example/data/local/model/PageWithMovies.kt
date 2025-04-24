package com.example.data.local.model

import androidx.room.Embedded
import androidx.room.Relation


data class PageWithMovies(
    @Embedded val pageData: PageDataEntity,
    @Relation(
        parentColumn = "page",
        entityColumn = "pageId"
    )
    val movies: List<MovieEntity>
)