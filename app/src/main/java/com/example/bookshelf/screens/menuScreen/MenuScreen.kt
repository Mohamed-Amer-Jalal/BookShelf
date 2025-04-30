package com.example.bookshelf.screens.menuScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.example.bookshelf.screens.queryScreen.GridList
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
        }) { innerPadding ->
        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is QueryUiState.Loading -> LoadingScreen()

            is QueryUiState.Success -> {
                GridList(
                    contentPadding = innerPadding,
                    viewModel = viewModel,
                    bookshelfList = uiState.bookshelfList,
                    modifier = modifier,
                    onDetailsClick = { viewModel.selectedBookId = it.id })
            }

            is QueryUiState.Error -> ErrorScreen(retryAction = { viewModel.getBooks() })
        }
    }
}