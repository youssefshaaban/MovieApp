package com.example.domain.usecase

import android.adservices.adid.AdId
import com.example.domain.entity.QueryCharacters
import com.example.domain.entity.movie.Movie
import com.example.domain.entity.movie.PageData
import com.example.domain.repositories.IMoviesRepository
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetMovieDetailUseCase @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
     suspend operator fun invoke(movieId:Int): Flow<Resource<Movie>> {
        return iMoviesRepository.getMovieDetail(movieId)
    }
}