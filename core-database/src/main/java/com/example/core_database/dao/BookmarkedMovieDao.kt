package com.example.core_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core_database.model.BookmarkedMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedMovieDao {
    @Query("SELECT * FROM bookmarked_movies")
    fun getAllBookmarkedMovies(): Flow<List<BookmarkedMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkedMovie(movie: BookmarkedMovie)

    @Delete
    suspend fun deleteBookmarkedMovie(movie: BookmarkedMovie)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_movies WHERE id = :movieId)")
    suspend fun isMovieBookmarked(movieId: String): Boolean

    @Query("DELETE FROM bookmarked_movies WHERE id = :movieId")
    suspend fun deleteBookmarkedMovieById(movieId: String)
}