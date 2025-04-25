package com.example.movieapp.ui.screens.movie_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.domain.entity.movie.Movie
import com.example.domain.usecase.GetMovieDetailUseCase
import com.example.domain.util.Resource
import com.example.movieapp.ui.navigation.NavRoute
import com.example.movieapp.ui.navigation.navigationCustomArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val getMovieDetailUseCase: GetMovieDetailUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()
    fun getMovieDetail(movieId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getMovieDetailUseCase.invoke(movieId = movieId.toInt()).collectLatest {result->
                when(result){
                    is Resource.Success->{
                        _state.update { it.copy(movie = result.data, isLoading = false) }
                    }
                    is Resource.Error->{
                        _state.update { it.copy(isError = true, isLoading = false) }
                    }
                }
            }
        }
    }
}

data class MovieDetailState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
   val movie: Movie? = null
)