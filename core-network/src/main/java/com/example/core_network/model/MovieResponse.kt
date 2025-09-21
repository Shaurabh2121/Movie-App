package com.example.core_network.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: String,
    val createdAt: Long,
    val title: String,
    val genre: List<String>,
    val rating: Rating,
    @SerializedName("release_date")
    val releaseDate: Long,
    @SerializedName("poster_url")
    val posterUrl: String,
    @SerializedName("duration_minutes")
    val durationMinutes: Long,
    val director: String,
    val cast: List<String>,
    @SerializedName("box_office_usd")
    val boxOfficeUsd: Long,
    val description: String,
)

data class Rating(
    val imdb: Double,
)
