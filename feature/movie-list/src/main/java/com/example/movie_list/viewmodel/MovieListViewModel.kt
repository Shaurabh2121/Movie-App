package com.example.movie_list.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.model.MovieDTO
import com.example.core_domain.usecase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import com.example.core_domain.usecase.BookmarkMovieUseCase
import com.example.core_domain.usecase.IsMovieBookmarkedUseCase
import com.example.core_domain.usecase.RemoveBookmarkUseCase
import kotlinx.coroutines.Job
import kotlin.collections.forEach

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val isBookmarkMovieUseCase: IsMovieBookmarkedUseCase,
    private val bookmarkMovieUseCase: BookmarkMovieUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase
) : ViewModel() {
    private val _movies = mutableStateOf<List<MovieDTO>>(emptyList())

    private val _filteredMovies = mutableStateOf<List<MovieDTO>>(emptyList())
    val filteredMovies: State<List<MovieDTO>> = _filteredMovies

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _sortOption = mutableStateOf(SortOption.TITLE)
    val sortOption: State<SortOption> = _sortOption

    private val _isLoading = mutableStateOf<UiState>(UiState.Loading)
    val isLoading: State<UiState> = _isLoading

    private val _bookmarkStates = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val bookmarkStates: State<Map<String, Boolean>> = _bookmarkStates

    private var currentDataFetchingJob: Job? = null

    init {
        fetchData()
    }

    fun fetchData() {
        currentDataFetchingJob?.cancel()

        currentDataFetchingJob = viewModelScope.launch {
            _isLoading.value = UiState.Loading
            try {
                val movieList = getMoviesUseCase()
                _movies.value = movieList
                loadBookmarkStates(movieList)
                applyFiltersAndSort()
                if (movieList.isEmpty()){
                    _isLoading.value = UiState.Error("Something went wrong")
                }else{
                    _isLoading.value = UiState.Success
                }
            } catch (e: Exception) {
                _movies.value = emptyList()
                applyFiltersAndSort()
                _isLoading.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun loadBookmarkStates(movies: List<MovieDTO>) {
        val bookmarkMap = mutableMapOf<String, Boolean>()

        movies.forEach { movie ->
            bookmarkMap[movie.id] = isBookmarkMovieUseCase(movie.id)
        }
        _bookmarkStates.value = bookmarkMap
    }

    fun toggleBookmark(movie: MovieDTO) {
        viewModelScope.launch {
            val isCurrentlyBookmarked = _bookmarkStates.value[movie.id] ?: false

            if (isCurrentlyBookmarked) {
                removeBookmarkUseCase(movie.id)
            } else {
                bookmarkMovieUseCase(movie)
            }

            _bookmarkStates.value = _bookmarkStates.value.toMutableMap().apply {
                this[movie.id] = !isCurrentlyBookmarked
            }
        }
    }


    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFiltersAndSort()
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
        var result = _movies.value

        // Apply search filter
        if (_searchQuery.value.isNotBlank()) {
            result = result.filter {
                it.title.contains(_searchQuery.value, ignoreCase = true)
            }
        }

        // Apply sorting
        result = when (_sortOption.value) {
            SortOption.TITLE -> result.sortedBy { it.title }
            SortOption.RELEASE_DATE -> result.sortedBy { it.releaseDate }
            SortOption.RATING -> result.sortedByDescending { it.rating.toDoubleOrNull() ?: 0.0 }
        }
        _filteredMovies.value = result
    }
}

enum class SortOption {
    TITLE, RELEASE_DATE, RATING
}

sealed class UiState(){
    object Loading: UiState()
    data class Error(val message: String): UiState()
    object Success: UiState()
}