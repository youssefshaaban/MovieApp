package com.example.data.mapper

import com.example.data.local.model.MovieEntity
import com.example.data.model.movie_list.Result
import com.example.domain.entity.movie.Movie
import com.example.domain.mapper.DataMapper
import javax.inject.Inject

class MovieRemotelToMovieDomain @Inject constructor() : DataMapper<Result, Movie> {
    override fun execute(data: Result): Movie {
       return Movie(
            id = data.id,
            original_title = data.original_title,
            title = data.title,
            poster_path = data.poster_path,
            overview = data.overview,
            release_date = data.release_date,
            vote_average = data.vote_average
        )
    }
}