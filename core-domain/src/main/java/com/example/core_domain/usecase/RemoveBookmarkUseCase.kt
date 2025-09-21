package com.example.core_domain.usecase

import com.example.core_domain.repository.MovieRepository
import javax.inject.Inject

class RemoveBookmarkUseCase @Inject constructor(private val movieRepository: MovieRepository)  {
    suspend operator fun invoke(movieId: String) {
        movieRepository.removeBookmark(movieId)
    }
}