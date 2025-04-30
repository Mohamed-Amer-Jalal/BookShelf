package com.example.bookshelf.screens.menuScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.screens.components.ErrorScreen
import com.example.bookshelf.screens.components.LoadingScreen
import com.example.bookshelf.screens.queryScreen.GridList
import com.example.bookshelf.screens.queryScreen.QueryScreen
import com.example.bookshelf.screens.queryScreen.QueryUiState
import com.example.bookshelf.screens.queryScreen.QueryViewModel

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    viewModel: QueryViewModel = viewModel(factory = QueryViewModel.Factory)
) {
    var isSearchActive by remember { mutableStateOf(false) }
    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            MainTopAppBar(
                isSearchActive = isSearchActive,
                onSearchClick = { isSearchActive = !isSearchActive },
                onFavoriteClick = { /* Handle favorite toggle */ },
                viewModel = viewModel
            )
        }
    ) { innerPadding ->
        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is QueryUiState.Loading -> LoadingScreen()

            is QueryUiState.Success -> {
                GridList(
                    contentPadding = innerPadding,
                    viewModel = viewModel,
                    bookshelfList = uiState.bookshelfList,
                    modifier = modifier,
                    onDetailsClick = { viewModel.selectedBookId = it.id }
                )
            }

            is QueryUiState.Error -> ErrorScreen(retryAction = { viewModel.getBooks() })
        }
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
    onSearchClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    viewModel: QueryViewModel
) {
    TopAppBar(
        title = {
            if (isSearchActive) {
                // عرض حقل البحث عندما يكون البحث مفعلًا
                QueryScreen(
                    viewModel = viewModel,
                )
            } else Text(stringResource(R.string.app_name))
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