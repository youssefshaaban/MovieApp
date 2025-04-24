package com.example.domain.entity.movie

data class PageData( val page: Int,
                     val results: List<Movie>,
                     val total_pages: Int,
                     val total_results: Int)
