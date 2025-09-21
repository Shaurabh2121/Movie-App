package com.example.core_domain.di

import com.example.core_domain.repository.MovieRepository
import com.example.core_domain.usecase.BookmarkMovieUseCase
import com.example.core_domain.usecase.GetBookmarkedMoviesUseCase
import com.example.core_domain.usecase.GetMovieDetailsUseCase
import com.example.core_domain.usecase.GetMoviesUseCase
import com.example.core_domain.usecase.IsMovieBookmarkedUseCase
import com.example.core_domain.usecase.RemoveBookmarkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetMoviesUseCase(movieRepository: MovieRepository): GetMoviesUseCase {
        return GetMoviesUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailUseCase(movieRepository: MovieRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideBookmarkMovieUseCase(movieRepository: MovieRepository): BookmarkMovieUseCase {
        return BookmarkMovieUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetBookmarkedMoviesUseCase(movieRepository: MovieRepository): GetBookmarkedMoviesUseCase {
        return GetBookmarkedMoviesUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideIsMovieBookmarkedUseCase(movieRepository: MovieRepository): IsMovieBookmarkedUseCase {
        return IsMovieBookmarkedUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveBookmarkUseCase(movieRepository: MovieRepository): RemoveBookmarkUseCase {
        return RemoveBookmarkUseCase(movieRepository)

    }


}