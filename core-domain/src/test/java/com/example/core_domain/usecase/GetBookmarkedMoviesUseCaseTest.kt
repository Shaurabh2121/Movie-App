package com.example.core_domain.usecase

import com.example.core_domain.model.MovieDTO
import com.example.core_domain.repository.MovieRepository
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetBookmarkedMoviesUseCaseTest {

    @Mock
    private lateinit var movieRepository: MovieRepository


    private lateinit var getBookmarkedMoviesUseCase: GetBookmarkedMoviesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getBookmarkedMoviesUseCase = GetBookmarkedMoviesUseCase(movieRepository)
    }

    @Test
    fun `invoke returns flow of bookmarked movies from repository`() {
        // Arrange
        val bookmarkedMovies = listOf(
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
        val bookmarkedMoviesFlow = flowOf(bookmarkedMovies)

        `when`(movieRepository.getBookmarkedMovies()).thenReturn(bookmarkedMoviesFlow)

        // Act
        val result = getBookmarkedMoviesUseCase()

        // Assert
        assertEquals(bookmarkedMoviesFlow, result)
    }

    @Test
    fun `invoke returns empty flow when repository returns empty`() {
        // Arrange
        val emptyFlow = flowOf(emptyList<MovieDTO>())
        `when`(movieRepository.getBookmarkedMovies()).thenReturn(emptyFlow)

        // Act
        val result = getBookmarkedMoviesUseCase()

        // Assert
        assertEquals(emptyFlow, result)
    }
}