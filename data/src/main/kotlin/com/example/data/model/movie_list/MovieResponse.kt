package com.example.data.model.movie_list

data class MovieResponse(
    val dates: Dates?=null,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)