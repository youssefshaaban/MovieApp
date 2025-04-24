package com.example.data.util


import com.example.data.local.model.MovieEntity
import com.example.data.model.movie_list.MovieResponse
import com.example.data.model.movie_list.Result
import com.example.domain.entity.movie.Movie
import com.google.gson.Gson
import retrofit2.Response

object TestUtil {
    val resultMovieResponse:Result= Gson().fromJson("{\n" +
            "  \"adult\": false,\n" +
            "  \"backdrop_path\": \"/fTrQsdMS2MUw00RnzH0r3JWHhts.jpg\",\n" +
            "  \"genre_ids\": [\n" +
            "    28,\n" +
            "    80,\n" +
            "    53\n" +
            "  ],\n" +
            "  \"id\": 1197306,\n" +
            "  \"original_language\": \"en\",\n" +
            "  \"original_title\": \"A Working Man\",\n" +
            "  \"overview\": \"Levon Cade left behind a decorated military career in the black ops to live a simple life working construction. But when his boss's daughter, who is like family to him, is taken by human traffickers, his search to bring her home uncovers a world of corruption far greater than he ever could have imagined.\",\n" +
            "  \"popularity\": 942.1209,\n" +
            "  \"poster_path\": \"/xUkUZ8eOnrOnnJAfusZUqKYZiDu.jpg\",\n" +
            "  \"release_date\": \"2025-03-26\",\n" +
            "  \"title\": \"A Working Man\",\n" +
            "  \"video\": false,\n" +
            "  \"vote_average\": 6.318,\n" +
            "  \"vote_count\": 415\n" +
            "}",Result::class.java)
    val defaultMovieResponse: MovieResponse = MovieResponse(page = 2, results = listOf(
        resultMovieResponse), total_results = 10, total_pages = 100)


    fun createMockGetMovieResponse(): Response<MovieResponse> {
        // Create a map of currencies for the mock response


        // Create a Response object with your mock data
        return Response.success(defaultMovieResponse)
    }

    fun createMovieEntity(pageId:Int) = MovieEntity(
        id = resultMovieResponse.id,
        original_title = resultMovieResponse.original_title,
        overview = resultMovieResponse.overview,
        poster_path = resultMovieResponse.poster_path,
        release_date = resultMovieResponse.release_date,
        title = resultMovieResponse.title,
        pageId = pageId
    )

    fun createMovie() = Movie(
        id = resultMovieResponse.id,
        original_title = resultMovieResponse.original_title,
        overview = resultMovieResponse.overview,
        poster_path = resultMovieResponse.poster_path,
        release_date = resultMovieResponse.release_date,
        title = resultMovieResponse.title,
    )

}