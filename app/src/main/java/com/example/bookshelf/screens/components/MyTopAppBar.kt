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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.bookshelf.R
import com.example.bookshelf.screens.queryScreen.QueryViewModel
import com.example.bookshelf.screens.queryScreen.SearchField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    isSearchActive: Boolean,
    onSearchToggle: () -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: QueryViewModel
) {
    val searchState by viewModel.searchState.collectAsState()
    TopAppBar(
        title = {
            if (isSearchActive) {
                SearchField(
                    query = searchState.query,
                    onQueryChange = { viewModel.updateSearchState(query = it) },
                    onSearch = { viewModel.getBooks(searchState.query) },
                    onSearchComplete = { onSearchToggle() }
                )
            } else Text(stringResource(R.string.app_name))
        },
        navigationIcon = {
            if (isSearchActive) {
                IconButton(onClick = onSearchToggle) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.close_search)
                    )
                }
            }
        },
        actions = {
            if (!isSearchActive) {
                IconButton(onClick = onSearchToggle) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                }
            }
            IconButton(onClick = onFavoritesClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.favorite)
                )
            }
        }
    )
}