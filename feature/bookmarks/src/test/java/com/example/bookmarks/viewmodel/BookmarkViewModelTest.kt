package com.example.bookmarks.viewmodel

import com.example.core_domain.model.MovieDTO
import com.example.core_domain.usecase.GetBookmarkedMoviesUseCase
import com.example.core_domain.usecase.RemoveBookmarkUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter // Added import
import kotlinx.coroutines.flow.first  // Added import
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class BookmarkViewModelTest {

    @Mock
    private lateinit var getBookmarkedMoviesUseCase: GetBookmarkedMoviesUseCase

    @Mock
    private lateinit var removeBookmarkUseCase: RemoveBookmarkUseCase

    private lateinit var viewModel: BookmarkViewModel

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
    fun `bookmarkedMovies should emit movies from use case`() = runTest {
        // Arrange
        val expectedBookmarkedMovies = listOf(
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

        `when`(getBookmarkedMoviesUseCase()).thenReturn(flowOf(expectedBookmarkedMovies))

        viewModel = BookmarkViewModel(getBookmarkedMoviesUseCase, removeBookmarkUseCase)

        // Act: Collect the first non-empty list emitted by the StateFlow.
        val actualBookmarkedMovies = viewModel.bookmarkedMovies
            .filter { it.isNotEmpty() } // Filter out initial empty list
            .first()                     // Collect the first list that is not empty

        // Assert
        assertEquals(expectedBookmarkedMovies, actualBookmarkedMovies)
        // Optionally, also assert the current value of the StateFlow
        assertEquals(expectedBookmarkedMovies, viewModel.bookmarkedMovies.value)
    }

    @Test
    fun `bookmarkedMovies should emit empty list when use case returns an empty flow`() = runTest {
        // Arrange
        `when`(getBookmarkedMoviesUseCase()).thenReturn(flowOf(emptyList()))

        viewModel = BookmarkViewModel(getBookmarkedMoviesUseCase, removeBookmarkUseCase)

        // Act
        // The StateFlow starts with emptyList() and the use case also provides an emptyList().
        // advanceUntilIdle() ensures that the collection from the use case (if any processing happens)
        // is completed.
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(emptyList<MovieDTO>(), viewModel.bookmarkedMovies.value)
    }

    @Test
    fun `removeBookmark should call removeBookmarkUseCase with correct movieId`() = runTest {
        // Arrange
        val movieId = "1"
        `when`(getBookmarkedMoviesUseCase()).thenReturn(flowOf(emptyList())) // Initial state for the ViewModel

        viewModel = BookmarkViewModel(getBookmarkedMoviesUseCase, removeBookmarkUseCase)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure initial collection of bookmarkedMovies completes

        // Act
        viewModel.removeBookmark(movieId)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure removeBookmark's coroutine completes

        // Assert
        verify(removeBookmarkUseCase).invoke(movieId)
    }
}


//@ExperimentalCoroutinesApi
//class BookmarkViewModelTest {
//
//    @Mock
//    private lateinit var getBookmarkedMoviesUseCase: GetBookmarkedMoviesUseCase
//
//    @Mock
//    private lateinit var removeBookmarkUseCase: RemoveBookmarkUseCase
//
//
//    private lateinit var viewModel: BookmarkViewModel
//
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setup() {
//        MockitoAnnotations.openMocks(this)
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `bookmarkedMovies should emit movies from use case`() = runTest {
//        // Arrange
//        val bookmarkedMovies = listOf(
//            MovieDTO(
//                id = "1",
//                title = "Test Movie 1",
//                releaseDate = "2023-01-01",
//                rating = "8.5",
//                poster = "poster_url_1",
//                description = "Description 1",
//                genre = "Action, Adventure",
//                director = "Director 1",
//                cast = "Actor 1, Actor 2"
//            ),
//            MovieDTO(
//                id = "2",
//                title = "Test Movie 2",
//                releaseDate = "2023-02-02",
//                rating = "7.5",
//                poster = "poster_url_2",
//                description = "Description 2",
//                genre = "Comedy",
//                director = "Director 2",
//                cast = "Actor 3, Actor 4"
//            )
//        )
//
//        `when`(getBookmarkedMoviesUseCase()).thenReturn(flowOf(bookmarkedMovies))
//
//        // Act
//        viewModel = BookmarkViewModel(getBookmarkedMoviesUseCase, removeBookmarkUseCase)
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Assert
//        assertEquals(bookmarkedMovies, viewModel.bookmarkedMovies.value)
//    }
//
//    @Test
//    fun `bookmarkedMovies should emit empty list when use case throws exception`() = runTest {
//        // Arrange
//        `when`(getBookmarkedMoviesUseCase()).thenReturn(flowOf(emptyList()))
//
//        // Act
//        viewModel = BookmarkViewModel(getBookmarkedMoviesUseCase, removeBookmarkUseCase)
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Assert
//        assertEquals(emptyList<MovieDTO>(), viewModel.bookmarkedMovies.value)
//    }
//
//    @Test
//    fun `removeBookmark should call removeBookmarkUseCase with correct movieId`() = runTest {
//        // Arrange
//        val movieId = "1"
//        `when`(getBookmarkedMoviesUseCase()).thenReturn(flowOf(emptyList()))
//        viewModel = BookmarkViewModel(getBookmarkedMoviesUseCase, removeBookmarkUseCase)
//
//        // Act
//        viewModel.removeBookmark(movieId)
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Assert
//        verify(removeBookmarkUseCase).invoke(movieId)
//    }
//}