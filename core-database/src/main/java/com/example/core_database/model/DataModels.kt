package com.example.core_database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Room Entities
@Entity(tableName = "bookmarked_movies")
data class BookmarkedMovie(
    @PrimaryKey val id: String,
    val title: String,
    val releaseDate: String,
    val rating: String,
    val poster: String?,
    val description: String? = null,
    val genre: String? = null,
    val director: String? = null,
    val cast: String? = null
)