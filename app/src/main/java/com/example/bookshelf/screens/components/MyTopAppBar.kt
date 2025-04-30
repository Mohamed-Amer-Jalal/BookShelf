package com.example.bookshelf.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.bookshelf.AppDestinations
import com.example.bookshelf.R
import com.example.bookshelf.screens.queryScreen.QueryScreen
import com.example.bookshelf.screens.queryScreen.QueryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    currentScreen: AppDestinations,
    canNavigateBack: Boolean,
    onNavigateUpClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = currentScreen.title) },
        navigationIcon = {
            if (canNavigateBack){
                IconButton(
                    onClick = onNavigateUpClicked
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.btn_try_again)
                    )
                }
            }
        }
    )
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
    onSearchClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    viewModel: QueryViewModel
) {
    TopAppBar(title = {
        if (isSearchActive) QueryScreen(viewModel = viewModel)
        else Text(stringResource(R.string.app_name))
    }, actions = {
        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search)
            )
        }
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = stringResource(R.string.favorite)
            )
        }
    })
}