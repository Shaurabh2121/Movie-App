package com.example.core_database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core_database.dao.BookmarkedMovieDao
import com.example.core_database.model.BookmarkedMovie

@Database(
    entities = [BookmarkedMovie::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun bookmarkedMovieDao(): BookmarkedMovieDao

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, MovieDatabase::class.java, "movie_database")
                .fallbackToDestructiveMigration(true)
                .build()
    }
}