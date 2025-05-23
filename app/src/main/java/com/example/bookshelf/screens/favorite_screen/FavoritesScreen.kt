package com.example.bookshelf.screens.favorite_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bookshelf.screens.components.ErrorScreen
import com.example.bookshelf.screens.components.LoadingScreen
import com.example.bookshelf.screens.queryScreen.GridList
import com.example.bookshelf.screens.queryScreen.QueryUiState
import com.example.bookshelf.screens.queryScreen.SearchViewModel

@Composable
fun FavoritesScreen(
    viewModel: SearchViewModel,
    bookshelfUiState: QueryUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Boolean
) {
    Column {
        if (!viewModel.favoriteBooks.isEmpty()) {
            when (bookshelfUiState) {
                is QueryUiState.Loading -> LoadingScreen(modifier = modifier)

                is QueryUiState.Success -> {
                    GridList(
                        viewModel = viewModel,
                        bookshelfList = bookshelfUiState.books,
                        modifier = modifier,
                        onDetailsClick = { /* Add navigation or details logic here */ }
                    )
                }

                is QueryUiState.Error -> ErrorScreen(retryAction = retryAction)
            }
        }
    }
}