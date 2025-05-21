package com.example.bookshelf.screens.menuScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.screens.components.ErrorScreen
import com.example.bookshelf.screens.components.LoadingScreen
import com.example.bookshelf.screens.components.MainTopAppBar
import com.example.bookshelf.screens.favorite_screen.FavoritesScreen
import com.example.bookshelf.screens.queryScreen.GridList
import com.example.bookshelf.screens.queryScreen.QueryUiState
import com.example.bookshelf.screens.queryScreen.QueryViewModel

enum class MenuScreenState { Menu, Favorites }

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    viewModel: QueryViewModel = viewModel(factory = QueryViewModel.Factory)
) {
    var isSearchActive by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf(MenuScreenState.Menu) }
    val uiState = viewModel.uiState.collectAsState().value
    val searchState by viewModel.searchState.collectAsState()

    Scaffold(
        topBar = {
            MainTopAppBar(
                isSearchActive = isSearchActive,
                onSearchToggle = {
                    isSearchActive = !isSearchActive
                    currentScreen = MenuScreenState.Menu
                },
                onFavoritesClick = { currentScreen = MenuScreenState.Favorites },
                viewModel = viewModel
            )
        }) { innerPadding ->
        when (currentScreen) {
            MenuScreenState.Menu -> {
                if (searchState.searchStarted) {
                    SearchContent(
                        uiState = uiState,
                        modifier = modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }

            MenuScreenState.Favorites -> {
                FavoritesScreen(
                    modifier = modifier.padding(innerPadding),
                    viewModel = viewModel,
                    bookshelfList = viewModel.uiState.collectAsState().value,
                    retryAction = { viewModel.getBooks() }
                )
            }
        }
    }
}

@Composable
private fun SearchContent(
    uiState: QueryUiState,
    modifier: Modifier = Modifier,
    viewModel: QueryViewModel
) {
    when (uiState) {
        is QueryUiState.Loading -> LoadingScreen(modifier = modifier)

        is QueryUiState.Success -> {
            Column(modifier = modifier) {
                Spacer(modifier = Modifier.height(16.dp))
                GridList(
                    contentPadding = PaddingValues(0.dp),
                    viewModel = viewModel,
                    bookshelfList = uiState.books,
                    modifier = Modifier.fillMaxSize(),
                    onDetailsClick = { viewModel.selectedBookId = it.id }
                )
            }
        }

        is QueryUiState.Error -> ErrorScreen(
            modifier = modifier,
            retryAction = { viewModel.getBooks() }
        )
    }
}