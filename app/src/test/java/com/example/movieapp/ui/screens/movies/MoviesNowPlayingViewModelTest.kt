package com.example.movieapp.ui.screens.movies

import com.example.domain.entity.movie.Movie
import com.example.domain.entity.movie.PageData
import com.example.domain.usecase.GetNowPlayingMoviesUseCase
import com.example.domain.util.Failure
import com.example.domain.util.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MoviesNowPlayingViewModelTest {



    private lateinit var viewModel: MoviesNowPlayingViewModel
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

    }

    @Test
    fun `test getNowPlayingMovie success`() = runTest {
        // Arrange
        val movie1 = Movie(id = 1, title = "Movie 1", overview = "Overview 1", poster_path = "", release_date = "")
        val movie2 = Movie(id = 2, title = "Movie 2", overview = "Overview 2", poster_path = "", release_date = "")
        val movieResponse = listOf(movie1, movie2)
        val page = 1
        coEvery { getNowPlayingMoviesUseCase(any()) } answers { flow {
            emit(Resource.Success(data = PageData(page = page, results = movieResponse,total_pages=10, total_results = 100)))
        }}
        viewModel = MoviesNowPlayingViewModel(getNowPlayingMoviesUseCase)
        // Act
        viewModel.getNowPlayingMovie()
        advanceUntilIdle()

        // Assert
        val state = viewModel.movieListState
        val movies = viewModel.moviesList

        assertEquals(state, MovieListState.IDLE)
        assertEquals(movies.size, 2)
        assertEquals(movies[0], movie1)
        assertEquals(movies[1], movie2)
        coVerify { getNowPlayingMoviesUseCase(page) }
    }

    @Test
    fun `test getNowPlayingMovie error`() = runTest {
        // Arrange
        val page = 1
        coEvery { getNowPlayingMoviesUseCase(page) } returns flow {
            emit(Resource.Error(Failure.NetworkConnection))
        }
        viewModel = MoviesNowPlayingViewModel(getNowPlayingMoviesUseCase)
        // Act
        viewModel.getNowPlayingMovie()
        advanceUntilIdle()

        // Assert
        val state = viewModel.movieListState
        assertTrue(state == MovieListState.ERROR)
        coVerify { getNowPlayingMoviesUseCase(page) }
    }

    @Test
    fun `test updateSearchQuery with no results`() = runTest {
        // Arrange
        val movie1 = Movie(id = 1, title = "Movie 1", overview = "Overview 1", poster_path = "", release_date = "")
        val movie2 = Movie(id = 2, title = "Movie 2", overview = "Overview 2", poster_path = "", release_date = "")
        val movieResponse = listOf(movie1, movie2)
        val page = 1
        coEvery { getNowPlayingMoviesUseCase(page) } returns flow {
            emit(Resource.Success(data = PageData(page = page, results = movieResponse,total_pages=10, total_results = 100)))
        }
        viewModel = MoviesNowPlayingViewModel(getNowPlayingMoviesUseCase)

        advanceUntilIdle()

        // Assert initial state
        assertEquals(viewModel.filteredMoviesList.size, 2)

        // Now search for a title that does not exist
        viewModel.updateSearchQuery("Nonexistent")
        advanceUntilIdle()

        // Assert filtered movies list is empty
        assertEquals(viewModel.filteredMoviesList.size, 0)
    }

    @Test
    fun `test updateSearchQuery with results`() = runTest {
        // Arrange
        val movie1 = Movie(id = 1, title = "Movie 1", overview = "Overview 1", poster_path = "", release_date = "")
        val movie2 = Movie(id = 2, title = "Movie 2", overview = "Overview 2", poster_path = "", release_date = "")
        val movieResponse = listOf(movie1, movie2)
        val page = 1
        coEvery { getNowPlayingMoviesUseCase(page) } returns flow {
            emit(Resource.Success(data = PageData(page = page, results = movieResponse,total_pages=10, total_results = 100)))
        }
        viewModel = MoviesNowPlayingViewModel(getNowPlayingMoviesUseCase)
        advanceUntilIdle()

        // Assert initial state
        assertEquals(viewModel.filteredMoviesList.size, 2)

        // Now search for a title that exists
        viewModel.updateSearchQuery("Movie 1")
        advanceUntilIdle()

        // Assert filtered list has the movie matching the search query
        assertEquals(viewModel.filteredMoviesList.size, 1)
        assertEquals(viewModel.filteredMoviesList[0], movie1)
    }

    @Test
    fun `test search reset`() = runTest {
        // Arrange
        val movie1 = Movie(id = 1, title = "Movie 1", overview = "Overview 1", poster_path = "", release_date = "")
        val movie2 = Movie(id = 2, title = "Movie 2", overview = "Overview 2", poster_path = "", release_date = "")
        val movieResponse = listOf(movie1, movie2)
        val page = 1
        coEvery { getNowPlayingMoviesUseCase(page) } returns flow {
            emit(Resource.Success(data = PageData(page = page, results = movieResponse,total_pages=10, total_results = 100)))
        }
        viewModel = MoviesNowPlayingViewModel(getNowPlayingMoviesUseCase)
        // Act
        advanceUntilIdle()

        // Assert initial state
        assertEquals(viewModel.filteredMoviesList.size, 2)

        // Apply a search filter
        viewModel.updateSearchQuery("Movie 1")
        advanceUntilIdle()

        // Assert filtered list size is 1 after search
        assertEquals(viewModel.filteredMoviesList.size, 1)

        // Now clear search query
        viewModel.updateSearchQuery("")
        advanceUntilIdle()

        // Assert filtered list size is 2 after clearing search
        assertEquals(viewModel.filteredMoviesList.size, 2)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}