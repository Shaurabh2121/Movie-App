package com.example.core_domain.usecase

import com.example.core_domain.model.MovieDTO
import com.example.core_domain.repository.MovieRepository
import javax.inject.Inject

class BookmarkMovieUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(movieDTO: MovieDTO) {
        movieRepository.bookmarkMovie(movieDTO)
    }
}