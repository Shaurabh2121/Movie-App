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

    private val _isLoading = mutableStateOf<UiState>(UiState.Loading)
    val isLoading: State<UiState> = _isLoading

    private val _isBookmarked = mutableStateOf(false)
    val isBookmarked: State<Boolean> = _isBookmarked

    fun loadMovieDetails(movieId: String) {
        viewModelScope.launch {
            _isLoading.value = UiState.Loading
            try {
                val movieDetails = getMovieDetailsUseCase(movieId)
                _movie.value = movieDetails
                _isBookmarked.value = isMovieBookmarkedUseCase(movieId)
                _isLoading.value = if (movieDetails == null) UiState.Error("Something went wrong") else UiState.Success
            } catch (e: Exception) {
                _isLoading.value = UiState.Error(e.message ?: "Unknown error")
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
sealed class UiState(){
    object Loading: UiState()
    data class Error(val message: String): UiState()
    object Success: UiState()
}