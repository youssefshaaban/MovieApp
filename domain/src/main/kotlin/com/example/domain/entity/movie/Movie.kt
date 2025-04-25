package com.example.domain.entity.movie

import kotlinx.serialization.Serializable


@Serializable
data class Movie(
    val id: Int,
    val original_title: String? = null,
    val overview: String? = null,
    val backdrop: String? = null,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double? = 0.0
)
