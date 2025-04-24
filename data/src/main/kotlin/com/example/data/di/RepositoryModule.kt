package com.example.data.di


import com.example.data.di.qulifier.MovieDataMapper
import com.example.data.di.qulifier.MoviesLocalDataMapper
import com.example.data.local.MovieDao
import com.example.data.local.model.MovieEntity
import com.example.data.local.model.PageWithMovies
import com.example.data.model.movie_list.MovieResponse
import com.example.data.remote.MovieAPI
import com.example.data.repositories.MovieRepositoryImp
import com.example.domain.entity.movie.PageData
import com.example.domain.mapper.DataMapper
import com.example.domain.repositories.IMoviesRepository
import com.example.domain.util.BatteryChecker
import com.example.domain.util.NetworkChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {


    @Provides
    fun provideCharactersRepository(
        movieAPI: MovieAPI,
        @MovieDataMapper dataMapper: DataMapper<PageWithMovies, PageData>,
        @MoviesLocalDataMapper dataMapperLocal: DataMapper<MovieResponse, List<MovieEntity>>,
        networkChecker: NetworkChecker,
        batteryChecker: BatteryChecker,
        dao: MovieDao
    ): IMoviesRepository {
        return MovieRepositoryImp(movieAPI,networkChecker, batteryChecker,dao,dataMapperLocal,dataMapper)
    }



}