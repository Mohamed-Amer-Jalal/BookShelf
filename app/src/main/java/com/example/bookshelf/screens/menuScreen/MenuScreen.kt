package com.example.bookshelf.screens.menuScreen

import android.R.attr.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.screens.queryScreen.GridList
import com.example.bookshelf.screens.queryScreen.QueryScreen
import com.example.bookshelf.screens.queryScreen.QueryUiState
import com.example.bookshelf.screens.queryScreen.QueryViewModel

@Composable
fun MenuScreen(
    viewModel: QueryViewModel = viewModel(factory = QueryViewModel.Factory),
    onBookClick: (Book) -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            MainTopAppBar(
                isSearchActive = isSearchActive,
                query = viewModel.query.value,
                onQueryChange = viewModel::updateQuery,
                onSearchToggle = { isSearchActive = !isSearchActive },
                onFavoriteToggle = { /*TODO*/ }
            )
        }
    ) { innerPadding ->
        GridList(
            contentPadding = innerPadding,
            viewModel = viewModel,
            modifier = TODO(),
            bookshelfList = TODO(),
            onBookClick = TODO()
        )
    }
}

/**
 * Main top app bar composable:
 * - Displays the app title and action icons for search and favorites.
 * - Invokes onSearchClick when the search icon is clicked.
 * - Invokes onFavoriteClick when the favorite icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    isSearchActive: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    TopAppBar(
        title = {
            when (isSearchActive) {
                true -> QueryScreen(
                    query = query,
                    onQueryChange = onQueryChange,
                    viewModel = TODO(),
                    onDetailsClick = TODO()
                )

                false -> Text(stringResource(R.string.app_name))
            }
        },
        actions = {
            IconButton(onClick = onSearchToggle) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.favorite)
                )
            }
        }
    )
}