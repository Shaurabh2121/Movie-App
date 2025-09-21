package com.example.movieapp.repository

import com.example.core_database.dao.BookmarkedMovieDao
import com.example.core_domain.model.MovieDTO
import com.example.core_domain.repository.MovieRepository
import com.example.core_network.remote.MovieApiService
import com.example.movieapp.mapper.toBookmarkedMovie
import com.example.movieapp.mapper.toMovieDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val bookmarkedMovieDao: BookmarkedMovieDao,
    private val movieApiService: MovieApiService
) : MovieRepository {

    override suspend fun getMovies(): List<MovieDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val response = movieApiService.getMovies()
                response.map { it.toMovieDTO() }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun getMovieDetails(id: String): MovieDTO? {
        return withContext(Dispatchers.IO) {
            try {
                movieApiService.getMovieDetails(id)?.toMovieDTO()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    override fun getBookmarkedMovies(): Flow<List<MovieDTO>> {
        return bookmarkedMovieDao.getAllBookmarkedMovies()
            .map { bookmarkedMovies ->
                bookmarkedMovies.map { it.toMovieDTO() }
            }
            .catch { e ->
                e.printStackTrace()
                emit(emptyList())
            }
    }

    override suspend fun isMovieBookmarked(movieId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                bookmarkedMovieDao.isMovieBookmarked(movieId)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun bookmarkMovie(movie: MovieDTO) {
        withContext(Dispatchers.IO) {
            try {
                bookmarkedMovieDao.insertBookmarkedMovie(movie.toBookmarkedMovie())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun removeBookmark(movieId: String) {
        withContext(Dispatchers.IO) {
            try {
                bookmarkedMovieDao.deleteBookmarkedMovieById(movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
