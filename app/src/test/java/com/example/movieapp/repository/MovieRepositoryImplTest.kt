package com.example.movieapp.repository

import com.example.core_database.dao.BookmarkedMovieDao
import com.example.core_database.model.BookmarkedMovie
import com.example.core_domain.model.MovieDTO
import com.example.core_network.model.Movie
import com.example.core_network.model.Rating
import com.example.core_network.remote.MovieApiService
import com.example.movieapp.mapper.toBookmarkedMovie
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class MovieRepositoryImplTest {

    @Mock
    private lateinit var bookmarkedMovieDao: BookmarkedMovieDao

    @Mock
    private lateinit var movieApiService: MovieApiService

    private lateinit var movieRepository: MovieRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        movieRepository = MovieRepositoryImpl(bookmarkedMovieDao, movieApiService)
    }

    @Test
    fun `getMovies returns mapped movies from API`() = runBlocking {
        // Arrange
        val networkMovies = listOf(
            Movie(
                id = "1",
                createdAt = 1672531200000, // 2023-01-01
                title = "Test Movie 1",
                genre = listOf("Action", "Adventure"),
                rating = Rating(imdb = 8.5),
                releaseDate = 1672531200000, // 2023-01-01
                posterUrl = "poster_url_1",
                durationMinutes = 120,
                director = "Director 1",
                cast = listOf("Actor 1", "Actor 2"),
                boxOfficeUsd = 1000000,
                description = "Description 1"
            ),
            Movie(
                id = "2",
                createdAt = 1675209600000, // 2023-02-01
                title = "Test Movie 2",
                genre = listOf("Comedy"),
                rating = Rating(imdb = 7.5),
                releaseDate = 1675209600000, // 2023-02-01
                posterUrl = "poster_url_2",
                durationMinutes = 90,
                director = "Director 2",
                cast = listOf("Actor 3", "Actor 4"),
                boxOfficeUsd = 2000000,
                description = "Description 2"
            )
        )

        `when`(movieApiService.getMovies()).thenReturn(networkMovies)

        // Act
        val result = movieRepository.getMovies()

        // Assert
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Test Movie 1", result[0].title)
        assertEquals("2023-01-01", result[0].releaseDate)
        assertEquals("8.5", result[0].rating)
        assertEquals("poster_url_1", result[0].poster)
        assertEquals("Description 1", result[0].description)
        assertEquals("Action, Adventure", result[0].genre)
        assertEquals("Director 1", result[0].director)
        assertEquals("Actor 1, Actor 2", result[0].cast)
    }

    @Test
    fun `getMovies returns empty list when API throws exception`() = runBlocking {
        // Arrange
        `when`(movieApiService.getMovies()).thenThrow(RuntimeException("Network error"))

        // Act
        val result = movieRepository.getMovies()

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMovieDetails returns mapped movie from API`() = runBlocking {
        // Arrange
        val movieId = "1"
        val networkMovie = Movie(
            id = movieId,
            createdAt = 1672531200000, // 2023-01-01
            title = "Test Movie",
            genre = listOf("Action", "Adventure"),
            rating = Rating(imdb = 8.5),
            releaseDate = 1672531200000, // 2023-01-01
            posterUrl = "poster_url",
            durationMinutes = 120,
            director = "Director",
            cast = listOf("Actor 1", "Actor 2"),
            boxOfficeUsd = 1000000,
            description = "Description"
        )

        `when`(movieApiService.getMovieDetails(movieId)).thenReturn(networkMovie)

        // Act
        val result = movieRepository.getMovieDetails(movieId)

        // Assert
        assertNotNull(result)
        assertEquals(movieId, result?.id)
        assertEquals("Test Movie", result?.title)
        assertEquals("2023-01-01", result?.releaseDate)
        assertEquals("8.5", result?.rating)
        assertEquals("poster_url", result?.poster)
        assertEquals("Description", result?.description)
        assertEquals("Action, Adventure", result?.genre)
        assertEquals("Director", result?.director)
        assertEquals("Actor 1, Actor 2", result?.cast)
    }

    @Test
    fun `getMovieDetails returns null when API throws exception`() = runBlocking {
        // Arrange
        val movieId = "1"
        `when`(movieApiService.getMovieDetails(movieId)).thenThrow(RuntimeException("Network error"))

        // Act
        val result = movieRepository.getMovieDetails(movieId)

        // Assert
        assertNull(result)
    }

    @Test
    fun `getBookmarkedMovies returns flow of mapped bookmarked movies`() = runBlocking {
        // Arrange
        val bookmarkedMovies = listOf(
            BookmarkedMovie(
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
            BookmarkedMovie(
                id = "2",
                title = "Test Movie 2",
                releaseDate = "2023-02-01",
                rating = "7.5",
                poster = "poster_url_2",
                description = "Description 2",
                genre = "Comedy",
                director = "Director 2",
                cast = "Actor 3, Actor 4"
            )
        )

        val bookmarkedMoviesFlow = flowOf(bookmarkedMovies)
        `when`(bookmarkedMovieDao.getAllBookmarkedMovies()).thenReturn(bookmarkedMoviesFlow)

        // Act
        val result = movieRepository.getBookmarkedMovies()

        // Assert
        val resultList = result.first()
        assertEquals(2, resultList.size)
        assertEquals("1", resultList[0].id)
        assertEquals("Test Movie 1", resultList[0].title)
        assertEquals("2023-01-01", resultList[0].releaseDate)
        assertEquals("8.5", resultList[0].rating)
        assertEquals("poster_url_1", resultList[0].poster)
        assertEquals("Description 1", resultList[0].description)
        assertEquals("Action, Adventure", resultList[0].genre)
        assertEquals("Director 1", resultList[0].director)
        assertEquals("Actor 1, Actor 2", resultList[0].cast)
    }

    @Test
    fun `isMovieBookmarked returns true when movie is bookmarked`() = runBlocking {
        // Arrange
        val movieId = "1"
        `when`(bookmarkedMovieDao.isMovieBookmarked(movieId)).thenReturn(true)

        // Act
        val result = movieRepository.isMovieBookmarked(movieId)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `isMovieBookmarked returns false when movie is not bookmarked`() = runBlocking {
        // Arrange
        val movieId = "1"
        `when`(bookmarkedMovieDao.isMovieBookmarked(movieId)).thenReturn(false)

        // Act
        val result = movieRepository.isMovieBookmarked(movieId)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `isMovieBookmarked returns false when DAO throws exception`() = runBlocking {
        // Arrange
        val movieId = "1"
        `when`(bookmarkedMovieDao.isMovieBookmarked(movieId)).thenThrow(RuntimeException("Database error"))

        // Act
        val result = movieRepository.isMovieBookmarked(movieId)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `bookmarkMovie calls DAO with mapped BookmarkedMovie`() = runBlocking {
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
        movieRepository.bookmarkMovie(movie)

        // Assert
        verify(bookmarkedMovieDao).insertBookmarkedMovie(movie.toBookmarkedMovie())
    }

    @Test
    fun `removeBookmark calls DAO with movie ID`() = runBlocking {
        // Arrange
        val movieId = "1"

        // Act
        movieRepository.removeBookmark(movieId)

        // Assert
        verify(bookmarkedMovieDao).deleteBookmarkedMovieById(movieId)
    }
}