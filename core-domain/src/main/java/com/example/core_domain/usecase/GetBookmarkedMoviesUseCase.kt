package com.example.core_domain.usecase

import com.example.core_domain.model.MovieDTO
import com.example.core_domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkedMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository)   {
    operator fun invoke(): Flow<List<MovieDTO>> {
        return movieRepository.getBookmarkedMovies()
    }
}