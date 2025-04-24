package com.example.data.remote


import com.example.data.model.movie_list.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface MovieAPI {

    @GET("3/movie/now_playing")
    suspend fun getMoviesNowPlayingList(
        @QueryMap queryMap: Map<String, String>
    ): Response<MovieResponse>



}