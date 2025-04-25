package com.example.movieapp.ui.screens.movie_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.movieapp.R
import com.example.movieapp.ui.BaseImageUrlLarg
import com.example.movieapp.ui.components.Loading

@Composable
fun MovieDetailScreen(movieId:String) {
    val movieDetail= hiltViewModel<MovieDetailViewModel>()
    val uiState by movieDetail.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        movieDetail.getMovieDetail(movieId)
    }
    if (uiState.isLoading){
        Loading(modifier = Modifier.fillMaxSize())
    }
    if (uiState.isError){
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.something_want_wrong))
        }
    }
    if (uiState.movie!=null){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 🖼 Backdrop or Poster
            AsyncImage(
                model = "${BaseImageUrlLarg}${uiState.movie?.backdrop ?: uiState.movie?.poster_path}",
                contentDescription = "Movie Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🎬 Movie Title
            Text(
                text = uiState.movie?.title?:"",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // 📅 Release Date & ⭐ Rating
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Release: ${uiState.movie?.release_date}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
                RatingBar(voteAverage = uiState.movie?.vote_average ?: 0.0)
            }

            // 📝 Overview
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
            Text(
                text = uiState.movie?.overview ?: "No description available.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }

}


@Composable
fun RatingBar(voteAverage: Double, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            val filled = index < (voteAverage / 2).toInt()
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (filled) Color.Yellow else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = " (${voteAverage}/10)",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

