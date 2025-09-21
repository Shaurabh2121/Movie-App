package com.example.core_domain.usecase

import com.example.core_domain.repository.MovieRepository
import javax.inject.Inject

class IsMovieBookmarkedUseCase @Inject constructor(private val movieRepository: MovieRepository)  {
    suspend operator fun invoke(movieId: String): Boolean {
        return movieRepository.isMovieBookmarked(movieId)
    }
}