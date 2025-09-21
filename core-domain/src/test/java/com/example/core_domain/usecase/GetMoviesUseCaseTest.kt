package com.example.core_domain.usecase

import com.example.core_domain.model.MovieDTO
import com.example.core_domain.repository.MovieRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetMoviesUseCaseTest {

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var getMoviesUseCase: GetMoviesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getMoviesUseCase = GetMoviesUseCase(movieRepository)
    }

    @Test
    fun `invoke returns movies from repository`() = runBlocking {
        // Arrange
        val expectedMovies = listOf(
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

        `when`(movieRepository.getMovies()).thenReturn(expectedMovies)

        // Act
        val result = getMoviesUseCase()

        // Assert
        assertEquals(expectedMovies, result)
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runBlocking {
        // Arrange
        val expectedMovies = emptyList<MovieDTO>()
        `when`(movieRepository.getMovies()).thenReturn(expectedMovies)

        // Act
        val result = getMoviesUseCase()

        // Assert
        assertEquals(expectedMovies, result)
    }
}