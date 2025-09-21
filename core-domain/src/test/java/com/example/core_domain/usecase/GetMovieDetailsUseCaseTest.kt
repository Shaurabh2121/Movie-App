package com.example.core_domain.usecase

import com.example.core_domain.model.MovieDTO
import com.example.core_domain.repository.MovieRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetMovieDetailsUseCaseTest {

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getMovieDetailsUseCase = GetMovieDetailsUseCase(movieRepository)
    }

    @Test
    fun `invoke returns movie details from repository when movie exists`() = runBlocking {
        // Arrange
        val movieId = "1"
        val expectedMovie = MovieDTO(
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

        `when`(movieRepository.getMovieDetails(movieId)).thenReturn(expectedMovie)

        // Act
        val result = getMovieDetailsUseCase(movieId)

        // Assert
        assertEquals(expectedMovie, result)
    }

    @Test
    fun `invoke returns null when movie does not exist`() = runBlocking {
        // Arrange
        val movieId = "999"
        `when`(movieRepository.getMovieDetails(movieId)).thenReturn(null)

        // Act
        val result = getMovieDetailsUseCase(movieId)

        // Assert
        assertNull(result)
    }
}