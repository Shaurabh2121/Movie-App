package com.example.movie_detail.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.model.MovieDTO
import com.example.core_domain.usecase.BookmarkMovieUseCase
import com.example.core_domain.usecase.GetMovieDetailsUseCase
import com.example.core_domain.usecase.IsMovieBookmarkedUseCase
import com.example.core_domain.usecase.RemoveBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val getMovieDetailsUseCase: GetMovieDetailsUseCase
,private val isMovieBookmarkedUseCase: IsMovieBookmarkedUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
    private val bookmarkMovieUseCase: BookmarkMovieUseCase) : ViewModel() {
    private val _movie = mutableStateOf<MovieDTO?>(null)
    val movie: State<MovieDTO?> = _movie

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isBookmarked = mutableStateOf(false)
    val isBookmarked: State<Boolean> = _isBookmarked

    fun loadMovieDetails(movieId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val movieDetails = getMovieDetailsUseCase(movieId)
                _movie.value = movieDetails
                _isBookmarked.value = isMovieBookmarkedUseCase(movieId)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleBookmark() {
        val currentMovie = _movie.value ?: return
        viewModelScope.launch {
            if (_isBookmarked.value) {
                removeBookmarkUseCase(currentMovie.id)
                _isBookmarked.value = false
            } else {
                bookmarkMovieUseCase(currentMovie)
                _isBookmarked.value = true
            }
        }
    }
}