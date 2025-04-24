package com.example.data.repositories

import com.example.data.local.MovieDao
import com.example.data.local.model.MovieEntity
import com.example.data.local.model.PageDataEntity
import com.example.data.local.model.PageWithMovies
import com.example.data.model.movie_list.MovieResponse
import com.example.data.remote.MovieAPI
import com.example.data.utils.apiCall
import com.example.domain.entity.QueryCharacters
import com.example.domain.entity.movie.PageData
import com.example.domain.mapper.DataMapper
import com.example.domain.repositories.IMoviesRepository
import com.example.domain.util.BatteryChecker
import com.example.domain.util.NetworkChecker
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class MovieRepositoryImp @Inject constructor(
    private val movieAPI: MovieAPI,
    private val networkChecker: NetworkChecker,
    private val batteryChecker: BatteryChecker,
    private val dao: MovieDao,
    private val dataMapperLocal: DataMapper<MovieResponse, List<MovieEntity>>,
    private val dataMapper: DataMapper<PageWithMovies, PageData>
) : IMoviesRepository {
    override suspend fun getMovieListNowPlaying(queryCharacters: QueryCharacters): Flow<Resource<PageData>> {
        return flow {
            var lastCashed: PageWithMovies? = null
            if (queryCharacters.page > 1) {
                lastCashed = dao.getPageWithMovies(queryCharacters.page - 1)
            }
            if (networkChecker.isConnected() && batteryChecker.isSufficient()) {
                val response =
                    apiCall { movieAPI.getMoviesNowPlayingList(queryCharacters.toQueryMap()) }
                when (response) {
                    is Resource.Success -> {
                        val movies = dataMapperLocal.execute(response.data)
                        val pageEntity = PageDataEntity(
                            page = queryCharacters.page,
                            totalPages = response.data.total_pages,
                            totalResults = response.data.total_results
                        )
                        dao.insertPage(pageEntity)
                        dao.insertAll(movies)
                        val cached = dao.getPageWithMovies(queryCharacters.page)
                        cached?.let {
                            emit(Resource.Success(dataMapper.execute(cached)))
                        }
                    }

                    is Resource.Error -> {
                        lastCashed?.let { cached->
                            emit(Resource.Success(dataMapper.execute(cached)))
                        }
                    }
                }
            } else {
                lastCashed?.let { cached ->
                    emit(Resource.Success(dataMapper.execute(cached)))
                }
            }
        }
    }


}