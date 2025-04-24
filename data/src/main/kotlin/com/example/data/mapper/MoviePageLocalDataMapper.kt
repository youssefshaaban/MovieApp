package com.example.data.mapper


import com.example.data.local.model.MovieEntity
import com.example.data.model.movie_list.MovieResponse
import com.example.domain.mapper.DataMapper
import javax.inject.Inject

class MoviePageLocalDataMapper @Inject constructor() : DataMapper<MovieResponse, List<MovieEntity>> {
    override fun execute(data: MovieResponse): List<MovieEntity> {
        return data.results.map { MovieEntity(pageId = data.page, id = it.id, original_title = it.original_title, title = it.title, release_date = it.release_date, poster_path = it.poster_path, overview = it.overview) }
    }
}