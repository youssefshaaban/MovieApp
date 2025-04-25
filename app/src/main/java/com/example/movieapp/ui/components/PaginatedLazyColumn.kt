package com.example.movieapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.domain.entity.movie.Movie
import com.example.movieapp.R
import com.example.movieapp.ui.screens.movies.MovieListState
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter


@Composable
fun PaginatedLazyColumn(
    items: PersistentList<Movie>,  // Using PersistentList for efficient state management
    loadMoreItems: () -> Unit,  // Function to load more items
    lazyGridState: LazyGridState,  // Track the scroll state of the LazyColumn
    buffer: Int = 2,  // Buffer to load more items when we get near the end
    movieListState: MovieListState,
    modifier: Modifier = Modifier,
    listContent: @Composable (Movie) -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            // Get the total number of items in the list
            val totalItemsCount = lazyGridState.layoutInfo.totalItemsCount
            // Get the index of the last visible item
            val lastVisibleItemIndex =
                lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Check if we have scrolled near the end of the list and more items should be loaded
            lastVisibleItemIndex >= (totalItemsCount - buffer) && movieListState!=MovieListState.PAGINATING
        }
    }
    LaunchedEffect(lazyGridState) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }  // Ensure that we load more items only when needed
            .collect {
                Log.e("call", "load more")
                loadMoreItems()
            }
    }
    // LazyColumn to display the list of items
    if (movieListState==MovieListState.LOADING) {
        Loading(modifier = Modifier.fillMaxSize())
    }
    if (movieListState==MovieListState.ERROR){
        Column (modifier=Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center) {
            Text(text = stringResource(R.string.something_want_wrong))
        }
    }
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize(),// Add padding for better visual spacing
        state = lazyGridState,
        columns = GridCells.Fixed(count = 2)
    ) {
        // Render each item in the list using a unique key
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            listContent(item)
        }
        item(
            key = movieListState,
        ) {
            if (movieListState==MovieListState.PAGINATING) {
                PaginationLoading()
            }

        }

    }

}