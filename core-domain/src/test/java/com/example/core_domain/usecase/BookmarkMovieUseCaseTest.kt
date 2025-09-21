package com.example.core_domain.usecase

import com.example.core_domain.model.MovieDTO
import com.example.core_domain.repository.MovieRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class BookmarkMovieUseCaseTest {

    @Mock
    private lateinit var movieRepository: MovieRepository


    private lateinit var bookmarkMovieUseCase: BookmarkMovieUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        bookmarkMovieUseCase = BookmarkMovieUseCase(movieRepository)
    }

    @Test
    fun `invoke calls bookmarkMovie on repository`() = runBlocking {
        // Arrange
        val movie = MovieDTO(
            id = "1",
            title = "Test Movie",
            releaseDate = "2023-01-01",
            rating = "8.5",
            poster = "poster_url",
            description = "Description",
            genre = "Action, Adventure",
            director = "Director",
            cast = "Actor 1, Actor 2"
        )

        // Act
        bookmarkMovieUseCase(movie)

        // Assert
        verify(movieRepository).bookmarkMovie(movie)
    }
}