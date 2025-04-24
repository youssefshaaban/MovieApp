package com.example.data.mapper


import com.example.data.local.model.PageWithMovies
import com.example.domain.entity.movie.Movie
import com.example.domain.entity.movie.PageData
import com.example.domain.mapper.DataMapper
import javax.inject.Inject

class MoviePageDataMapper @Inject constructor() : DataMapper<PageWithMovies, PageData> {
    override fun execute(data: PageWithMovies): PageData {
        return PageData(
            page =data.pageData.page,
            total_pages = data.pageData.totalPages,
            total_results = data.pageData.totalResults,
            results = data.movies.map { Movie(
                id = it.id,
                original_title = it.original_title,
                title = it.title,
                poster_path = it.poster_path,
                overview = it.overview,
                release_date = it.release_date
            ) })
    }
}