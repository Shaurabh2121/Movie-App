package com.example.core_domain.model

// Data Models
data class MovieDTO(
    val id: String,
    val title: String,
    val releaseDate: String,
    val rating: String,
    val poster: String?,
    val description: String? = null,
    val genre: String? = null,
    val director: String? = null,
    val cast: String? = null
)

