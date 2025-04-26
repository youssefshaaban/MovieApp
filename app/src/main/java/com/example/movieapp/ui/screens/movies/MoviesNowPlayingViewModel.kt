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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesNowPlayingViewModel @Inject constructor(private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase) :
    ViewModel() {
    private var page = 1
    private var canPaginate = false
    private val MAXPAGE = 500
    var movieListState by mutableStateOf(MovieListState.IDLE)
    val moviesList = mutableListOf<Movie>()
    private val _searchQueryFlow = MutableStateFlow("")
    val searchQueryFlow: StateFlow<String> = _searchQueryFlow.asStateFlow()
    private val _filteredMoviesList = mutableStateListOf<Movie>()
    val filteredMoviesList: List<Movie>
        get() = _filteredMoviesList

    init {
        getNowPlayingMovie()
        viewModelScope.launch {
            _searchQueryFlow
                .debounce(2000) // Wait for 3 seconds
                .distinctUntilChanged()
                .collect { query ->
                    updateSearchQuery(query)
                }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQueryFlow.value = newQuery
    }

    fun getNowPlayingMovie() = viewModelScope.launch {
        if (page == 1 || canPaginate) {
            movieListState = if (page == 1) MovieListState.LOADING else MovieListState.PAGINATING
            getNowPlayingMoviesUseCase(page).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        canPaginate = page <= MAXPAGE
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
        if (query.isEmpty()) {
            canPaginate = page <= MAXPAGE
            _filteredMoviesList.clear()
            _filteredMoviesList.addAll(moviesList)
        } else {
            canPaginate = false
            filterMovies(query)
        }

    }

    // Filter movies based on the search query
    private fun filterMovies(queryString: String) {
        _filteredMoviesList.clear()
        _filteredMoviesList.addAll(moviesList.filter { movie ->
            movie.title.contains(queryString, ignoreCase = true)
        })
    }

}