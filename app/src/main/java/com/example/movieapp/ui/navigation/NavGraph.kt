package com.example.movieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.domain.entity.movie.Movie
import com.example.movieapp.ui.screens.movies.MovieNowPlayingScreen


@Composable
fun NavGraph(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavRoute.MoviesList
    ) {
        addMovieListScreen(navController, this)

        addMovieDetailScreen(navController, this)
    }
}

fun addMovieDetailScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable<NavRoute.MovieDetail>(typeMap = mapOf(navigationCustomArgument<Movie>()) ) { navBackStackEntry ->

    }
}

fun addMovieListScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable<NavRoute.MoviesList> {
        MovieNowPlayingScreen { movie ->
            navController.navigate(NavRoute.MovieDetail(movie))
        }
    }

}
