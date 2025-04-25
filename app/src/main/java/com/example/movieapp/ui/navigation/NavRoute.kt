package com.example.movieapp.ui.navigation

import com.example.domain.entity.movie.Movie
import kotlinx.serialization.Serializable


sealed class NavRoute {

    @Serializable
    data object MoviesList: NavRoute()

    @Serializable
    data class MovieDetail(val movieId: String): NavRoute()

}

