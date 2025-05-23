package com.example.bookshelf.screens.menuScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.bookshelf.NavigationItem
import com.example.bookshelf.R

@Composable
fun MenuScreen(
    currentScreen: NavigationItem,
    onSearchClick: () -> Unit,
    onFavClick: () -> Unit
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                selected = currentScreen == NavigationItem.Search,
                icon = { Icons.Filled.Search },
                label = { Text(stringResource(R.string.search)) },
                onClick = onSearchClick
            )
            item(
                selected = currentScreen == NavigationItem.Favorites,
                icon = { Icons.Filled.Favorite },
                label = { Text(stringResource(R.string.favorite)) },
                onClick = onFavClick
            )
        }
    ) {

    }
}