package com.example.movieapp.ui.screens.movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.movie.Movie
import com.example.domain.usecase.GetNowPlayingMoviesUseCase
import com.example.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesNowPlayingViewModel @Inject constructor(private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase) :
    ViewModel() {
    private var page = 1
    private var canPaginate = false
    var movieListState by mutableStateOf(MovieListState.IDLE)
    val moviesList = mutableListOf<Movie>()
    private val _searchQuery = mutableStateOf("")
    private val _filteredMoviesList = mutableStateListOf<Movie>()
    val filteredMoviesList: List<Movie>
        get() = _filteredMoviesList

    init {
        getNowPlayingMovie()
    }


    fun getNowPlayingMovie() = viewModelScope.launch {
        if (page == 1 || canPaginate) {
            movieListState = if (page == 1) MovieListState.LOADING else MovieListState.PAGINATING
            getNowPlayingMoviesUseCase(page).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        canPaginate = page <= 500
                        if (page == result.data.page) {
                            if (page == 1) {
                                moviesList.clear()
                                _filteredMoviesList.clear()
                                moviesList.addAll(result.data.results)
                                _filteredMoviesList.addAll(result.data.results)
                            } else {
                                moviesList.addAll(result.data.results)
                                _filteredMoviesList.addAll(result.data.results)
                            }
                            if (canPaginate) {
                                page++
                            }

                        }
                        movieListState = MovieListState.IDLE
                    }

                    is Resource.Error -> {
                        movieListState = if (moviesList.isEmpty()) MovieListState.ERROR
                        else MovieListState.IDLE
                    }
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            _filteredMoviesList.clear()
            _filteredMoviesList.addAll(moviesList)
        } else {
            filterMovies()
        }

    }

    // Filter movies based on the search query
    private fun filterMovies() {
        _filteredMoviesList.clear()
        _filteredMoviesList.addAll(moviesList.filter { movie ->
            movie.title.contains(_searchQuery.value, ignoreCase = true)
        })
    }

}