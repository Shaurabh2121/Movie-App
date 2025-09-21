package com.example.movie_list.viewmodel

import com.example.core_domain.model.MovieDTO
import com.example.core_domain.usecase.BookmarkMovieUseCase
import com.example.core_domain.usecase.GetMoviesUseCase
import com.example.core_domain.usecase.IsMovieBookmarkedUseCase
import com.example.core_domain.usecase.RemoveBookmarkUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn

@ExperimentalCoroutinesApi
class MovieListViewModelTest {

    @Mock
    private lateinit var getMoviesUseCase: GetMoviesUseCase
    
    @Mock
    private lateinit var isBookmarkMovieUseCase: IsMovieBookmarkedUseCase
    
    @Mock
    private lateinit var bookmarkMovieUseCase: BookmarkMovieUseCase
    
    @Mock
    private lateinit var removeBookmarkUseCase: RemoveBookmarkUseCase

    private lateinit var viewModel: MovieListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load movies and update state to success when movies are available`() = runTest {
        // Arrange
        val movies = listOf(
            MovieDTO(
                id = "1",
                title = "Test Movie 1",
                releaseDate = "2023-01-01",
                rating = "8.5",
                poster = "poster_url_1",
                description = "Description 1",
                genre = "Action, Adventure",
                director = "Director 1",
                cast = "Actor 1, Actor 2"
            ),
            MovieDTO(
                id = "2",
                title = "Test Movie 2",
                releaseDate = "2023-02-02",
                rating = "7.5",
                poster = "poster_url_2",
                description = "Description 2",
                genre = "Comedy",
                director = "Director 2",
                cast = "Actor 3, Actor 4"
            )
        )

        `when`(getMoviesUseCase()).thenReturn(movies)

        // Act
        viewModel = MovieListViewModel(getMoviesUseCase, isBookmarkMovieUseCase, bookmarkMovieUseCase, removeBookmarkUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(movies, viewModel.filteredMovies.value)
        assertEquals(UiState.Success, viewModel.isLoading.value)
    }

    @Test
    fun `init should update state to error when movies cannot be loaded`() = runTest {
        // Arrange
        val errorMessage = "Failed to load movies"
        `when`(getMoviesUseCase()).thenThrow(RuntimeException(errorMessage))

        // Act
        viewModel = MovieListViewModel(getMoviesUseCase, isBookmarkMovieUseCase, bookmarkMovieUseCase, removeBookmarkUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(emptyList<MovieDTO>(), viewModel.filteredMovies.value)
        assertTrue(viewModel.isLoading.value is UiState.Error)
        assertEquals(errorMessage, (viewModel.isLoading.value as UiState.Error).message)
    }

    @Test
    fun `init should update state to error with default message when exception has no message`() = runTest {
        // Arrange
        `when`(getMoviesUseCase()).thenThrow(RuntimeException())

        // Act
        viewModel = MovieListViewModel(getMoviesUseCase, isBookmarkMovieUseCase, bookmarkMovieUseCase, removeBookmarkUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(emptyList<MovieDTO>(), viewModel.filteredMovies.value)
        assertTrue(viewModel.isLoading.value is UiState.Error)
        assertEquals("Unknown error", (viewModel.isLoading.value as UiState.Error).message)
    }

    @Test
    fun `init should update state to empty when movies list is empty`() = runTest {
        // Arrange
        `when`(getMoviesUseCase()).thenReturn(emptyList())

        // Act
        viewModel = MovieListViewModel(getMoviesUseCase, isBookmarkMovieUseCase, bookmarkMovieUseCase, removeBookmarkUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(emptyList<MovieDTO>(), viewModel.filteredMovies.value)
        assertEquals("Something went wrong", (viewModel.isLoading.value as UiState.Error).message)
    }

    @Test
    fun `refreshMovies should update movies and state when successful`() = runTest {
        // Arrange
        val initialMovies = listOf(
            MovieDTO(
                id = "1",
                title = "Initial Movie",
                releaseDate = "2023-01-01",
                rating = "8.5",
                poster = "poster_url_1",
                description = "Description 1",
                genre = "Action, Adventure",
                director = "Director 1",
                cast = "Actor 1, Actor 2"
            )
        )

        val refreshedMovies = listOf(
            MovieDTO(
                id = "2",
                title = "Refreshed Movie",
                releaseDate = "2023-02-02",
                rating = "7.5",
                poster = "poster_url_2",
                description = "Description 2",
                genre = "Comedy",
                director = "Director 2",
                cast = "Actor 3, Actor 4"
            )
        )

        `when`(getMoviesUseCase()).thenReturn(initialMovies).thenReturn(refreshedMovies)

        viewModel = MovieListViewModel(getMoviesUseCase, isBookmarkMovieUseCase, bookmarkMovieUseCase, removeBookmarkUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify initial state
        assertEquals(initialMovies, viewModel.filteredMovies.value)
        assertEquals(UiState.Success, viewModel.isLoading.value)

        // Act - refresh movies
        viewModel.fetchData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(refreshedMovies, viewModel.filteredMovies.value)
        assertEquals(UiState.Success, viewModel.isLoading.value)
    }

}