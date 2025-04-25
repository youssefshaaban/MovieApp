package com.example.data.repositories

import com.example.data.local.MovieDao
import com.example.data.local.model.MovieEntity
import com.example.data.local.model.PageDataEntity
import com.example.data.local.model.PageWithMovies
import com.example.data.model.movie_list.MovieResponse
import com.example.data.model.movie_list.Result
import com.example.data.remote.MovieAPI
import com.example.data.util.MainCoroutineRuleTest
import com.example.data.util.TestUtil
import com.example.domain.entity.QueryCharacters
import com.example.domain.entity.movie.Movie
import com.example.domain.entity.movie.PageData
import com.example.domain.mapper.DataMapper
import com.example.domain.util.BatteryChecker
import com.example.domain.util.NetworkChecker
import com.example.domain.util.Resource
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
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
    private val dataMapperMovieLocal: DataMapper<MovieEntity, Movie> = mockk()
    private val dataMapperMovieRemote: DataMapper<Result, Movie> = mockk()

    private lateinit var repository: MovieRepositoryImp

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Before
    fun setUp() {
        repository = MovieRepositoryImp(
            movieAPI,
            networkChecker,
            batteryChecker,
            movieDao,
            dataMapperLocal,
            dataMapper,
            dataMapperMovieLocal,
            dataMapperMovieRemote
        )
    }


    @Test
    fun `getMovieListNowPlaying should return cached data when available`() = runTest {
        // Given
        val queryCharacters = QueryCharacters(page = 1)
        val cachedPageWithMovies = mockk<PageWithMovies>()
        val expectedPageData = mockk<PageData>()

        coEvery { movieDao.getPageWithMovies(queryCharacters.page) } returns cachedPageWithMovies
        coEvery { dataMapper.execute(cachedPageWithMovies) } returns expectedPageData

        // When
        val result = repository.getMovieListNowPlaying(queryCharacters).first()

        // Then
        assertEquals(Resource.Success(expectedPageData), result)
        coVerify(exactly = 0) { movieAPI.getMoviesNowPlayingList(any()) }
    }


    @Test
    fun `getMovieListNowPlaying should fetch from API when no cache and network available`() =
        runTest {
            // Given
            val queryCharacters = QueryCharacters(page = 1)
            val apiResponse = TestUtil.createMockGetMovieResponse()
            val movieEntities = listOf(TestUtil.createMovieEntity(1))
            val pageEntity = PageDataEntity(page = 1, totalResults = 10, totalPages = 100)
            val pageWithMovies = PageWithMovies(pageEntity, movieEntities)
            val pageData = PageData(
                page = 1,
                results = listOf(TestUtil.createMovie()),
                total_results = 10,
                total_pages = 100
            )

            coEvery { movieDao.getPageWithMovies(queryCharacters.page) } returnsMany listOf(
                null,
                pageWithMovies
            )
            coEvery { networkChecker.isConnected() } returns true
            coEvery { batteryChecker.isSufficient() } returns true
            coEvery { movieAPI.getMoviesNowPlayingList(any()) } returns apiResponse
            coEvery { dataMapperLocal.execute(apiResponse.body()!!) } returns movieEntities
            coEvery { movieDao.insertPage(any()) } just runs
            coEvery { movieDao.insertAll(any()) } just runs
            coEvery { dataMapper.execute(pageWithMovies) } returns pageData

            // When
            val result = repository.getMovieListNowPlaying(queryCharacters).first()

            // Then
            assertEquals(Resource.Success(pageData), result)
            coVerify { movieAPI.getMoviesNowPlayingList(any()) }
            coVerify { movieDao.insertPage(any()) }
            coVerify { movieDao.insertAll(any()) }
        }

    @Test
    fun `getMovieListNowPlaying on API error returns erorr`() = runTest {
        val queryCharacters = QueryCharacters(page = 1)
        coEvery { movieDao.getPageWithMovies(1) } returns null
        coEvery { networkChecker.isConnected() } returns true
        coEvery { batteryChecker.isSufficient() } returns true
        coEvery { movieAPI.getMoviesNowPlayingList(any()) } throws RuntimeException()

        val result =
            repository.getMovieListNowPlaying(queryCharacters).first()
        assert(result is Resource.Error)
        coVerify { movieAPI.getMoviesNowPlayingList(queryCharacters.toQueryMap()) }
    }


    @Test
    fun `getMovieDetail should return cached data when available`() = runTest {
        // Given

        val cachedMovieEntity = mockk<MovieEntity>()
        val expectedMovie = mockk<Movie>()

        coEvery { movieDao.getMovieById(any()) } returns cachedMovieEntity
        coEvery { dataMapperMovieLocal.execute(cachedMovieEntity) } returns expectedMovie

        // When
        val result = repository.getMovieDetail(1).first()

        // Then
        assertEquals(Resource.Success(expectedMovie), result)
        coVerify(exactly = 0) { movieAPI.getMovieById(any()) }
    }


    @Test
    fun `getMovieDetail on API error returns error`() = runTest {
        coEvery { movieDao.getMovieById(any()) } returns null
        coEvery { networkChecker.isConnected() } returns true
        coEvery { batteryChecker.isSufficient() } returns true
        coEvery { movieAPI.getMovieById(any()) } throws RuntimeException()

        val result =
            repository.getMovieDetail(1).first()
        assert(result is Resource.Error)
        coVerify { movieAPI.getMovieById(1) }
    }

}