package com.example.data.remote


import com.example.data.model.movie_list.MovieResponse
import com.example.data.model.movie_list.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface MovieAPI {

    @GET("3/movie/now_playing")
    suspend fun getMoviesNowPlayingList(
        @QueryMap queryMap: Map<String, String>
    ): Response<MovieResponse>


    @GET("3/movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movie_id: Int
    ): Response<Result>

}