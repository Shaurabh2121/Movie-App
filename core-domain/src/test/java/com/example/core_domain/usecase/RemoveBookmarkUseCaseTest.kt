package com.example.core_domain.usecase

import com.example.core_domain.repository.MovieRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class RemoveBookmarkUseCaseTest {

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var removeBookmarkUseCase: RemoveBookmarkUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        removeBookmarkUseCase = RemoveBookmarkUseCase(movieRepository)
    }

    @Test
    fun `invoke calls removeBookmark on repository`() = runBlocking {
        // Arrange
        val movieId = "1"

        // Act
        removeBookmarkUseCase(movieId)

        // Assert
        verify(movieRepository).removeBookmark(movieId)
    }
}