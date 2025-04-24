package com.example.data.model.movie_list



data class Result(
    val adult: Boolean? = null,
    val backdrop_path: String?=null,
    val genre_ids: List<Int>?= listOf(),
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)