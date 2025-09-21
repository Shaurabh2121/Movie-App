package com.example.movieapp.mapper

import com.example.core_database.model.BookmarkedMovie
import com.example.core_domain.model.MovieDTO
import com.example.core_network.model.Movie
import java.text.SimpleDateFormat
import java.util.Date

fun MovieDTO.toBookmarkedMovie(): BookmarkedMovie {
    return BookmarkedMovie(
        id = id,
        title = title,
        releaseDate = releaseDate,
        rating = rating,
        poster = poster,
        description = description,
        genre = genre,
        director = director,
        cast = cast
    )
}

fun BookmarkedMovie.toMovieDTO(): MovieDTO {
    return MovieDTO(
        id = id,
        title = title,
        releaseDate = releaseDate,
        rating = rating,
        poster = poster,
        description = description,
        genre = genre,
        director = director,
        cast = cast
    )
}

fun Movie.toMovieDTO(): MovieDTO {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
    return MovieDTO(
        id = this.id,
        title = this.title,
        releaseDate = dateFormatter.format(Date(this.releaseDate)),
        rating = this.rating.imdb.toString(), // Convert Double to String
        poster = this.posterUrl,
        description = this.description,
        genre = this.genre.joinToString(", "), // Join list of genres into a single String
        director = this.director,
        cast = this.cast.joinToString(", ") // Join list of cast members into a single String
    )
}