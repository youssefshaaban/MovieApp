package com.example.data.repositories

import com.example.data.local.MovieDao
import com.example.data.local.model.MovieEntity
import com.example.data.local.model.PageDataEntity
import com.example.data.local.model.PageWithMovies
import com.example.data.model.movie_list.MovieResponse
import com.example.data.remote.MovieAPI
import com.example.data.util.MainCoroutineRuleTest
import com.example.data.util.TestUtil
import com.example.domain.entity.QueryCharacters
import com.example.domain.entity.movie.PageData
import com.example.domain.mapper.DataMapper
import com.example.domain.util.BatteryChecker
import com.example.domain.util.NetworkChecker
import com.example.domain.util.Resource
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MovieRepositoryImpTest {

    @get:Rule
    val coroutineRule = MainCoroutineRuleTest() // Custom JUnit Rule to control coroutines

    private val movieAPI: MovieAPI = mockk()
    private val networkChecker: NetworkChecker = mockk()
    private val batteryChecker: BatteryChecker = mockk()
    private val movieDao: MovieDao = mockk()
    private val dataMapperLocal: DataMapper<MovieResponse, List<MovieEntity>> = mockk()
    private val dataMapper: DataMapper<PageWithMovies, PageData> = mockk()

    private lateinit var repository: MovieRepositoryImp

    @Before
    fun setUp() {
        repository = MovieRepositoryImp(
            movieAPI,
            networkChecker,
            batteryChecker,
            movieDao,
            dataMapperLocal,
            dataMapper
        )
    }

    private val query = QueryCharacters(page = 2)

    @Test
    fun `getMovieListNowPlaying returns success from API`() = runTest {
        val apiResponse = TestUtil.createMockGetMovieResponse()
        val movieEntities = listOf(TestUtil.createMovieEntity(2))
        val pageEntity = PageDataEntity(page = 2, totalResults = 10, totalPages = 100)
        val pageWithMovies = PageWithMovies(pageEntity, movieEntities)
        val pageData = PageData(page = 2, results = listOf(TestUtil.createMovie()), 10, 100)

        coEvery { networkChecker.isConnected() } returns true
        coEvery { batteryChecker.isSufficient() } returns true
        coEvery { movieAPI.getMoviesNowPlayingList(any()) } returns apiResponse
        coEvery { dataMapperLocal.execute(apiResponse.body()!!) } returns movieEntities
        coJustRun { movieDao.insertPage(any()) }
        coJustRun { movieDao.insertAll(any()) }
        coEvery { movieDao.getPageWithMovies(1) } returns null
        coEvery { movieDao.getPageWithMovies(2) } returns pageWithMovies
        coEvery { dataMapper.execute(pageWithMovies) } returns pageData
        val result =
            repository.getMovieListNowPlaying(query).first()
        assert(result is Resource.Success)
        assertEquals(
            dataMapperLocal.execute(apiResponse.body()!!),
            movieEntities
        )
        coVerify { movieDao.insertAll(movieEntities) }
        coVerify { movieDao.insertPage(pageEntity) }
        coVerify { movieAPI.getMoviesNowPlayingList(query.toQueryMap()) }
    }


    @Test
    fun `getMovieListNowPlaying falls back to cache on API error`() = runTest {
        val pageEntity = PageDataEntity(1, totalResults = 10, totalPages =  100)
        val movieEntities = listOf(TestUtil.createMovieEntity(1))
        val cached = PageWithMovies(pageEntity, movieEntities)
        val mapped = PageData(1, total_results = 10, total_pages = 100, results = listOf(TestUtil.createMovie()))

        coEvery { networkChecker.isConnected() } returns true
        coEvery { batteryChecker.isSufficient() } returns true
        coEvery { movieAPI.getMoviesNowPlayingList(any()) } throws RuntimeException()
        coEvery { movieDao.getPageWithMovies(1) } returns cached
        coEvery { dataMapper.execute(cached) } returns mapped
        val result =
            repository.getMovieListNowPlaying(query).first()
        assert(result is Resource.Success)
        assertEquals((result as Resource.Success).data,mapped)
        coVerify { movieAPI.getMoviesNowPlayingList(query.toQueryMap()) }
    }

    @Test
    fun `getMovieListNowPlaying returns cache when offline or low battery`() = runTest {
        val pageEntity = PageDataEntity(1, totalResults = 10, totalPages =  100)
        val movieEntities = listOf(TestUtil.createMovieEntity(1))
        val cached = PageWithMovies(pageEntity, movieEntities)
        val mapped = PageData(1, total_results = 10, total_pages = 100, results = listOf(TestUtil.createMovie()))
        coEvery { networkChecker.isConnected() } returns false
        coEvery { batteryChecker.isSufficient() } returns false
        coEvery { movieDao.getPageWithMovies(1) } returns cached
        coEvery { dataMapper.execute(cached) } returns mapped

        val result =
            repository.getMovieListNowPlaying(query).first()
        assert(result is Resource.Success)
        assertEquals((result as Resource.Success).data,mapped)
    }
}