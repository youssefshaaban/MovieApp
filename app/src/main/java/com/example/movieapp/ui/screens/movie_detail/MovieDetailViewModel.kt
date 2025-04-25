package com.example.movieapp.ui.screens.movie_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.domain.entity.movie.Movie
import com.example.movieapp.ui.navigation.NavRoute
import com.example.movieapp.ui.navigation.navigationCustomArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor (private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val movie: Movie by lazy {
        savedStateHandle
            .toRoute<NavRoute.MovieDetail>(
                mapOf(navigationCustomArgument<Movie>())
            ).movie
    }

    private val _state = MutableStateFlow(movie)
    val state: StateFlow<Movie> = _state.asStateFlow()
}