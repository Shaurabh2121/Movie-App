package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookmarks.screen.BookmarkScreen
import com.example.movie_detail.screen.MovieDetailScreen
import com.example.movie_list.screen.MovieListScreen
import com.example.movieapp.ui.theme.MovieAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieAppTheme {
                MovieApp()
            }
        }
    }
}

@Composable
fun MovieApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "movie_list"
    ) {

        composable("movie_list") {
            MovieListScreen(
                onMovieClick = { movieId ->
                    navController.navigate("movie_detail/$movieId")
                },
                onBookmarksClick = {
                    navController.navigate("bookmarks")
                }
            )
        }

        composable("bookmarks") {
            BookmarkScreen(
                onMovieClick = { movieId ->
                    navController.navigate("movie_detail/$movieId")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            "movie_detail/{movieId}",
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}