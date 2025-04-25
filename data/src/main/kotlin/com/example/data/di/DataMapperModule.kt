package com.example.data.di

import com.example.data.di.qulifier.MovieDataMapper
import com.example.data.di.qulifier.MovieLocalDataMapper
import com.example.data.di.qulifier.MovieRemoteDataMapper
import com.example.data.di.qulifier.MoviesLocalDataMapper
import com.example.data.local.model.MovieEntity
import com.example.data.local.model.PageWithMovies
import com.example.data.mapper.MovieLocalToMovieDomain
import com.example.data.mapper.MovieRemotelToMovieDomain
import com.example.data.mapper.MoviesPageDataMapper
import com.example.data.mapper.MoviesPageLocalDataMapper
import com.example.data.model.movie_list.MovieResponse
import com.example.data.model.movie_list.Result
import com.example.domain.entity.movie.Movie
import com.example.domain.entity.movie.PageData
import com.example.domain.mapper.DataMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent


@Module
@InstallIn(ActivityRetainedComponent::class)
object MapperModule {

    @Provides
    @MovieDataMapper
    fun provideMoviePageDataMapper(): DataMapper<PageWithMovies, PageData> {
        return MoviesPageDataMapper()
    }

    @Provides
    @MoviesLocalDataMapper
    fun provideMoviesLocal(): DataMapper<MovieResponse, List<MovieEntity>> {
        return MoviesPageLocalDataMapper()
    }

    @Provides
    @MovieLocalDataMapper
    fun provideMovieLocal(): DataMapper<MovieEntity, Movie> {
        return MovieLocalToMovieDomain()
    }

    @Provides
    @MovieRemoteDataMapper
    fun provideMovieRemote(): DataMapper<Result, Movie> {
        return MovieRemotelToMovieDomain()
    }


}