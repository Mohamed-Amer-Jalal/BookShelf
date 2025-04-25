package com.example.bookshelf.screens.menuScreen

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.screens.queryScreen.QueryScreen

@Composable
fun MenuScreen(onSearchClick: () -> Unit, onFavoriteClick: () -> Unit) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                onSearchClick = onSearchClick,
                onFavoriteClick = onFavoriteClick,
                isSearchActive = TODO(),
                query = TODO(),
                onQueryChange = TODO()
            )
        }
    ) { innerPadding ->

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
    onSearchClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isSearchActive: Boolean,
    query: String,
    onQueryChange: (String) -> Unit
) {
    TopAppBar(
        title = {
            when (isSearchActive) {
                true -> QueryScreen(query = query, onQueryChange = onQueryChange)

                false -> Text(stringResource(R.string.app_name))
            }
        },
        actions = {
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
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    MainTopAppBar(
        onSearchClick = {},
        onFavoriteClick = {},
        isSearchActive = false,
        query = "",
        onQueryChange = {}
    )
}