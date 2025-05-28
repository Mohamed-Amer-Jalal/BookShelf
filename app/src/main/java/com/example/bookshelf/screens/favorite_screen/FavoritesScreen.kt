package com.example.bookshelf.screens.favorite_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bookshelf.screens.components.BookUiState
import com.example.bookshelf.screens.components.ErrorScreen
import com.example.bookshelf.screens.components.LoadingScreen
import com.example.bookshelf.screens.queryScreen.GridList
import com.example.bookshelf.screens.queryScreen.SearchViewModel

@Composable
fun FavoritesScreen(
    viewModel: SearchViewModel,
    bookshelfUiState: BookUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Boolean
) {
    Column {
        if (viewModel.favoriteBooks.isNotEmpty()) {
            when (bookshelfUiState) {
                is BookUiState.Loading -> LoadingScreen(modifier = modifier)

                is BookUiState.ListSuccess -> {
                    GridList(
                        viewModel = viewModel,
                        bookshelfList = bookshelfUiState.books,
                        modifier = modifier,
                        onDetailsClick = { /* Add navigation or details logic here */ }
                    )
                }

                else -> ErrorScreen(retryAction = retryAction)
            }
        }
    }
}