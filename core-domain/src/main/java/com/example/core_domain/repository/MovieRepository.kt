package com.example.core_domain.repository

import com.example.core_domain.model.MovieDTO
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovies(): List<MovieDTO>

    suspend fun getMovieDetails(id: String): MovieDTO?

    fun getBookmarkedMovies(): Flow<List<MovieDTO>>

    suspend fun isMovieBookmarked(movieId: String): Boolean

    suspend fun bookmarkMovie(movie: MovieDTO)

    suspend fun removeBookmark(movieId: String)
}