package com.example.domain.entity.movie

data class Movie(
    val id: Int,
    val original_title: String? = null,
    val overview: String? = null,
    val backdrop: String? = null,
    val poster_path: String,
    val release_date: String,
    val title: String
)
