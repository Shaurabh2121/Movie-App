package com.example.movie_list.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.core_domain.R
import com.example.core_domain.model.MovieDTO
import com.example.movie_list.viewmodel.MovieListViewModel
import com.example.movie_list.viewmodel.SortOption
import com.example.movie_list.viewmodel.UiState

@Composable
fun MovieListScreen(
   viewModel: MovieListViewModel = hiltViewModel(),
   onMovieClick: (String) -> Unit,
   onBookmarksClick : () -> Unit
) {
   val movies by viewModel.filteredMovies
   val searchQuery by viewModel.searchQuery
   val sortOption by viewModel.sortOption
   val isLoading by viewModel.isLoading
   val bookmarkStates by viewModel.bookmarkStates

   var showSortDialog by remember { mutableStateOf(false) }
   val keyboardController = LocalSoftwareKeyboardController.current
   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(16.dp)
   ) {
      Spacer(modifier = Modifier.height(16.dp))
      // Top App Bar
      Row(
         modifier = Modifier.fillMaxWidth(),
         horizontalArrangement = Arrangement.SpaceBetween,
         verticalAlignment = Alignment.CenterVertically
      ) {
         Text(
            text = "Movies",
            style = MaterialTheme.typography.headlineMedium
         )

         Row {
            IconButton(onClick = { onBookmarksClick() }) {
               Icon(
                  imageVector = Icons.Default.FavoriteBorder,
                  contentDescription = "View Bookmarks",
                  tint = Color.Red
               )
            }

            IconButton(onClick = { showSortDialog = true }) {
               Image(
                  painter = painterResource(R.drawable.sort),
                  contentDescription = "Sort",
                  modifier = Modifier.size(24.dp)
               )
            }
         }
      }

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
         value = searchQuery,
         onValueChange = viewModel::updateSearchQuery,
         label = { Text("Search movies...") },
         leadingIcon = {
            Icon(
               imageVector = Icons.Default.Search,
               contentDescription = "Search"
            )
         },
         trailingIcon = {
            if (searchQuery.isNotEmpty()) {
               IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                  Icon(
                     imageVector = Icons.Default.Close,
                     contentDescription = "Clear search"
                  )
               }
            }
         },
         modifier = Modifier.fillMaxWidth(),
         colors = OutlinedTextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.primary),
         keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
         ),
         keyboardActions = KeyboardActions(
            onDone = {
               keyboardController?.hide()
            }
         )
      )

      Spacer(modifier = Modifier.height(16.dp))

      when (isLoading) {
          is UiState.Error -> {
             Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
             ) {
                Text("Error: ${(isLoading as UiState.Error).message}", color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.fetchData() }) {
                   Text("Retry")
                }
             }
          }
          UiState.Loading -> {
             Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
             ) {
                CircularProgressIndicator()
             }
          }
          UiState.Success -> {
             LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
             ) {
                items(movies) { movie ->
                   MovieItem(
                      movie = movie,
                      isBookmarked = bookmarkStates[movie.id] ?: false,
                      onClick = { onMovieClick(movie.id) },
                      onBookmarkClick = { viewModel.toggleBookmark(movie) }
                   )
                }
             }
          }
      }
   }

   // Sort Dialog
   if (showSortDialog) {
      AlertDialog(
         onDismissRequest = { showSortDialog = false },
         title = { Text("Sort by") },
         text = {
            Column {
               SortOption.values().forEach { option ->
                  Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                           viewModel.updateSortOption(option)
                           showSortDialog = false
                        }
                        .padding(vertical = 8.dp),
                     verticalAlignment = Alignment.CenterVertically
                  ) {
                     RadioButton(
                        selected = sortOption == option,
                        onClick = {
                           viewModel.updateSortOption(option)
                           showSortDialog = false
                        }
                     )
                     Spacer(modifier = Modifier.width(8.dp))
                     Text(
                        text = when (option) {
                           SortOption.TITLE -> "Title"
                           SortOption.RELEASE_DATE -> "Release Date"
                           SortOption.RATING -> "Rating"
                        }
                     )
                  }
               }
            }
         },
         confirmButton = {
            TextButton(onClick = { showSortDialog = false }) {
               Text("Cancel")
            }
         }
      )
   }
}


@Composable
fun MovieItem(
   movie: MovieDTO,
   isBookmarked: Boolean,
   onClick: () -> Unit,
   onBookmarkClick: () -> Unit
) {
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .clickable { onClick() },
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
   ) {
      Box(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
      ) {
         Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
         ) {
            AsyncImage(
               model = movie.poster,
               contentDescription = "${movie.title} poster",
               modifier = Modifier
                  .size(60.dp)
                  .clip(CircleShape),
               contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
               modifier = Modifier
                  .weight(1f)
                  .padding(end = 48.dp) // Add padding to avoid overlap with bookmark icon
            ) {
               Text(
                  text = movie.title,
                  style = MaterialTheme.typography.titleMedium,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis
               )
               Spacer(modifier = Modifier.height(4.dp))
               Text(
                  text = "Release: ${movie.releaseDate}",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
               )
               Spacer(modifier = Modifier.height(4.dp))
               Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                     imageVector = Icons.Default.Star,
                     contentDescription = "Rating",
                     tint = Color(0xFFFFD700),
                     modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                     text = movie.rating,
                     style = MaterialTheme.typography.bodyMedium,
                     color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
               }
            }
         }

         // Bookmark Icon - Top Right Corner
         IconButton(
            onClick = onBookmarkClick,
            modifier = Modifier
               .align(Alignment.TopEnd)
               .size(40.dp)
         ) {
            Icon(
               imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
               contentDescription = if (isBookmarked) "Remove from bookmarks" else "Add to bookmarks",
               tint = if (isBookmarked) Color.Red else Color.Gray,
               modifier = Modifier.size(24.dp)
            )
         }
      }
   }
}