package com.example.movie_detail.viewmodel

import com.example.core_domain.model.MovieDTO
import com.example.core_domain.usecase.BookmarkMovieUseCase
import com.example.core_domain.usecase.GetMovieDetailsUseCase
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MovieDetailViewModelTest {

    @Mock
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    @Mock
    private lateinit var isMovieBookmarkedUseCase: IsMovieBookmarkedUseCase

    @Mock
    private lateinit var removeBookmarkUseCase: RemoveBookmarkUseCase

    @Mock
    private lateinit var bookmarkMovieUseCase: BookmarkMovieUseCase

    private lateinit var viewModel: MovieDetailViewModel

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
    fun `loadMovieDetails should update movie state with movie details`() = runTest {
        // Arrange
        val movieId = "1"
        val movie = MovieDTO(
            id = movieId,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            rating = "8.5",
            poster = "poster_url",
            description = "Description",
            genre = "Action, Adventure",
            director = "Director",
            cast = "Actor 1, Actor 2"
        )

        `when`(getMovieDetailsUseCase(movieId)).thenReturn(movie)
        `when`(isMovieBookmarkedUseCase(movieId)).thenReturn(false)

        viewModel = MovieDetailViewModel(
            getMovieDetailsUseCase,
            isMovieBookmarkedUseCase,
            removeBookmarkUseCase,
            bookmarkMovieUseCase
        )

        // Act
        viewModel.loadMovieDetails(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(movie, viewModel.movie.value)
        assertEquals(UiState.Success, viewModel.isLoading.value)
        assertFalse(viewModel.isBookmarked.value)
    }

    @Test
    fun `loadMovieDetails should update isBookmarked state when movie is bookmarked`() = runTest {
        // Arrange
        val movieId = "1"
        val movie = MovieDTO(
            id = movieId,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            rating = "8.5",
            poster = "poster_url",
            description = "Description",
            genre = "Action, Adventure",
            director = "Director",
            cast = "Actor 1, Actor 2"
        )

        `when`(getMovieDetailsUseCase(movieId)).thenReturn(movie)
        `when`(isMovieBookmarkedUseCase(movieId)).thenReturn(true)

        viewModel = MovieDetailViewModel(
            getMovieDetailsUseCase,
            isMovieBookmarkedUseCase,
            removeBookmarkUseCase,
            bookmarkMovieUseCase
        )

        // Act
        viewModel.loadMovieDetails(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(movie, viewModel.movie.value)
        assertEquals(UiState.Success, viewModel.isLoading.value)
        assertTrue(viewModel.isBookmarked.value)
    }

    @Test
    fun `loadMovieDetails should set error state when movie details cannot be loaded`() = runTest {
        // Arrange
        val movieId = "1"
        `when`(getMovieDetailsUseCase(movieId)).thenReturn(null)

        viewModel = MovieDetailViewModel(
            getMovieDetailsUseCase,
            isMovieBookmarkedUseCase,
            removeBookmarkUseCase,
            bookmarkMovieUseCase
        )

        // Act
        viewModel.loadMovieDetails(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertNull(viewModel.movie.value)
        assertTrue(viewModel.isLoading.value is UiState.Error)
        assertEquals("Something went wrong", (viewModel.isLoading.value as UiState.Error).message)
    }

    @Test
    fun `toggleBookmark should bookmark movie when not bookmarked`() = runTest {
        // Arrange
        val movieId = "1"
        val movie = MovieDTO(
            id = movieId,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            rating = "8.5",
            poster = "poster_url",
            description = "Description",
            genre = "Action, Adventure",
            director = "Director",
            cast = "Actor 1, Actor 2"
        )

        `when`(getMovieDetailsUseCase(movieId)).thenReturn(movie)
        `when`(isMovieBookmarkedUseCase(movieId)).thenReturn(false)

        viewModel = MovieDetailViewModel(
            getMovieDetailsUseCase,
            isMovieBookmarkedUseCase,
            removeBookmarkUseCase,
            bookmarkMovieUseCase
        )

        viewModel.loadMovieDetails(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.toggleBookmark()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(bookmarkMovieUseCase).invoke(movie)
        assertTrue(viewModel.isBookmarked.value)
    }

    @Test
    fun `toggleBookmark should remove bookmark when bookmarked`() = runTest {
        // Arrange
        val movieId = "1"
        val movie = MovieDTO(
            id = movieId,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            rating = "8.5",
            poster = "poster_url",
            description = "Description",
            genre = "Action, Adventure",
            director = "Director",
            cast = "Actor 1, Actor 2"
        )

        `when`(getMovieDetailsUseCase(movieId)).thenReturn(movie)
        `when`(isMovieBookmarkedUseCase(movieId)).thenReturn(true)

        viewModel = MovieDetailViewModel(
            getMovieDetailsUseCase,
            isMovieBookmarkedUseCase,
            removeBookmarkUseCase,
            bookmarkMovieUseCase
        )

        viewModel.loadMovieDetails(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.toggleBookmark()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(removeBookmarkUseCase).invoke(movieId)
        assertFalse(viewModel.isBookmarked.value)
    }

    @Test
    fun `toggleBookmark should do nothing when movie is null`() = runTest {
        // Arrange
        viewModel = MovieDetailViewModel(
            getMovieDetailsUseCase,
            isMovieBookmarkedUseCase,
            removeBookmarkUseCase,
            bookmarkMovieUseCase
        )

        // Act
        viewModel.toggleBookmark()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert - No exceptions should be thrown
        assertFalse(viewModel.isBookmarked.value)
    }
}