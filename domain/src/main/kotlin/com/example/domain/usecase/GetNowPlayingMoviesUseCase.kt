package com.example.domain.usecase

import com.example.domain.entity.QueryCharacters
import com.example.domain.entity.movie.PageData
import com.example.domain.repositories.IMoviesRepository
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetNowPlayingMoviesUseCase @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
     suspend operator fun invoke(page:Int): Flow<Resource<PageData>> {
        return iMoviesRepository.getMovieListNowPlaying(QueryCharacters(page=page))
    }
}