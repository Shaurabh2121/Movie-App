package com.example.movie_detail.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.core_domain.R
import com.example.movie_detail.viewmodel.MovieDetailViewModel
import com.example.movie_detail.viewmodel.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: String,
    viewModel: MovieDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val movie by viewModel.movie
    val isLoading by viewModel.isLoading
    val isBookmarked by viewModel.isBookmarked

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    when(isLoading){
        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Error: ${(isLoading as UiState.Error).message}", color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    viewModel.loadMovieDetails(movieId)
                }) {
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
            movie?.let { movieDetails ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Top App Bar
                    TopAppBar(
                        title = { Text(movieDetails.title) },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { viewModel.toggleBookmark() }) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = if (isBookmarked) "Remove Bookmark" else "Add Bookmark",
                                    tint = if (isBookmarked) Color.Red else Color.Gray
                                )
                            }
                        }
                    )

                    // Movie Details
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Movie Poster
                        AsyncImage(
                            model = movieDetails.poster,
                            contentDescription = "${movieDetails.title} poster",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.place_holder),
                            error = painterResource(R.drawable.place_holder)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Title
                        Text(
                            text = movieDetails.title,
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = movieDetails.rating,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Release Date
                        DetailRow("Release Date", movieDetails.releaseDate)

                        // Genre
                        movieDetails.genre?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            DetailRow("Genre", it)
                        }

                        // Director
                        movieDetails.director?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            DetailRow("Director", it)
                        }

                        // Cast
                        movieDetails.cast?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            DetailRow("Cast", it)
                        }

                        // Description
                        movieDetails.description?.let {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.7f)
        )
    }
}