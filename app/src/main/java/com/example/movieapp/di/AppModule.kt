package com.example.movieapp.di

import com.example.core_database.dao.BookmarkedMovieDao
import com.example.core_domain.repository.MovieRepository
import com.example.core_network.remote.MovieApiService
import com.example.movieapp.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(bookmarkedMovieDao: BookmarkedMovieDao, movieApiService: MovieApiService): MovieRepository {
        return MovieRepositoryImpl(bookmarkedMovieDao,movieApiService)
    }


}