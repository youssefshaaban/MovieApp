package com.example.movieapp.ui.screens.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.domain.entity.movie.Movie
import com.example.movieapp.ui.BaseImageUrlGrid
import com.example.movieapp.ui.components.PaginatedLazyColumn
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MovieNowPlayingScreen(onClickMovie: (Movie) -> Unit) {
    val viewModel: MoviesNowPlayingViewModel = hiltViewModel()
    val uiState = viewModel.movieListState
    val moviesList = viewModel.filteredMoviesList
    val lazyColumnListState = rememberLazyGridState()
    val searchQuery = viewModel.searchQueryFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Movies",
                color = Color.Red,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
            )
        }

        // Search bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = searchQuery.value,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search Movies") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                placeholder = { Text("Search by title...") }
            )
        }

        PaginatedLazyColumn(
            items = moviesList.toPersistentList(),  // Convert the list to a PersistentList
            loadMoreItems = viewModel::getNowPlayingMovie,
            lazyGridState = lazyColumnListState,
            movieListState = uiState,
            modifier = Modifier.fillMaxSize()
        ) { item ->
            MovieItem(item) { movie ->
                onClickMovie(movie)
            }
        }

    }

}

@Composable
fun MovieItem(movie: Movie, onClick: (Movie) -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onClick(movie) }
    ) {
        AsyncImage(
            model = "${BaseImageUrlGrid}${movie.poster_path}",
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .height(150.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = movie.title,
            color = Color.Black,
            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
        )
        Text(
            text = movie.release_date,
            color = Color.Black,
            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
        )

    }


}

