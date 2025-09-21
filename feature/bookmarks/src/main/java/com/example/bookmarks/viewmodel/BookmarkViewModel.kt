package com.example.bookmarks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.model.MovieDTO
import com.example.core_domain.usecase.GetBookmarkedMoviesUseCase
import com.example.core_domain.usecase.RemoveBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class BookmarkViewModel @Inject constructor(private val getBookmarkedMoviesUseCase: GetBookmarkedMoviesUseCase,private val removeBookmarkUseCase: RemoveBookmarkUseCase) : ViewModel() {


    val bookmarkedMovies: StateFlow<List<MovieDTO>> = getBookmarkedMoviesUseCase()
        .catch { e ->
            e.printStackTrace()
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun removeBookmark(movieId: String) {
        viewModelScope.launch {
            removeBookmarkUseCase(movieId)
        }
    }
}