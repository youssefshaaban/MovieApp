package com.example.domain.repositories


import com.example.domain.entity.QueryCharacters
import com.example.domain.entity.movie.PageData
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow


interface IMoviesRepository {
    suspend fun getMovieListNowPlaying(queryCharacters: QueryCharacters):Flow<Resource<PageData>>
}