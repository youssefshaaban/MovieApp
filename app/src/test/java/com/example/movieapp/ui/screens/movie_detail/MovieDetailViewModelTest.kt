package com.example.movieapp.ui.screens.movie_detail


import com.example.domain.entity.movie.Movie
import com.example.domain.usecase.GetMovieDetailUseCase
import com.example.domain.util.Failure
import com.example.domain.util.Resource
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MovieDetailViewModelTest {


    private lateinit var getMovieDetailUseCase: GetMovieDetailUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: MovieDetailViewModel

    @Before
    fun setup() {
        getMovieDetailUseCase= mockk()
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieDetailViewModel(getMovieDetailUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `test getMovieDetail success`() = runTest {
        val movieId = "1"
        val movie = Movie(id = 1, title = "Test Movie", overview = "Test overview", poster_path = "", release_date = "")
        coEvery { getMovieDetailUseCase.invoke(movieId.toInt()) } returns flow {
            emit(Resource.Success(movie))
        }
        viewModel.getMovieDetail(movieId)
        advanceUntilIdle()
        val state = viewModel.state.value
        assertEquals(state.movie, movie)
        assertEquals(state.isLoading, false)
        assertEquals(state.isError, false)

        coVerify { getMovieDetailUseCase.invoke(movieId.toInt()) }
    }

    @Test
    fun `test getMovieDetail error`() = runTest {
        val movieId = "1"
        coEvery { getMovieDetailUseCase.invoke(movieId.toInt()) } returns flow {
            emit(Resource.Error(Failure.NetworkConnection))
        }
        viewModel.getMovieDetail(movieId)
        advanceUntilIdle()
        val state = viewModel.state.value
        assertTrue(state.isError)
        assertEquals(state.isLoading, false)
        assertEquals(state.movie, null)

        coVerify { getMovieDetailUseCase.invoke(movieId.toInt()) }
    }

    @Test
    fun `test loading state during getMovieDetail`() = runTest {
        val movieId = "1"
        coEvery { getMovieDetailUseCase.invoke(movieId.toInt()) } returns flow {

            emit(Resource.Success(Movie(id = 1, title = "Test Movie", overview = "Test overview", poster_path = "", release_date = "")))
        }
        viewModel.getMovieDetail(movieId)
        advanceUntilIdle()
        val state = viewModel.state.value
        assertEquals(state.isLoading, false)
        assertEquals(state.isError, false)
        assertTrue(state.movie != null)
    }

}
