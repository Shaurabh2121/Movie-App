package com.example.core_network.remote

import com.example.core_network.model.Movie
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApiService {

    @GET("movies")
    suspend fun getMovies(): List<Movie>
    @GET("movies/{id}")
    suspend fun getMovieDetails(@Path("id") id: String): Movie?
}