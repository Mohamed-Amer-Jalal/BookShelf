package com.example.bookshelf.screens.favorite_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.bookshelf.R
import com.example.bookshelf.screens.components.ErrorScreen
import com.example.bookshelf.screens.components.LoadingScreen
import com.example.bookshelf.screens.queryScreen.GridList
import com.example.bookshelf.screens.queryScreen.QueryUiState
import com.example.bookshelf.screens.queryScreen.QueryViewModel

@Composable
fun FavoritesScreen(
    viewModel: QueryViewModel,
    bookshelfUiState: QueryUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        if (!viewModel.favoriteBooks.isEmpty()) {
            when (bookshelfUiState) {
                is QueryUiState.Loading -> LoadingScreen()

                is QueryUiState.Success -> GridList(
                    bookshelfList = bookshelfUiState.bookshelfList,
                    viewModel = viewModel,
                    modifier = modifier,
                    onDetailsClick = { }
                )

                is QueryUiState.Error -> ErrorScreen(retryAction = retryAction)
            }
        } else {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_favorite_books))
            }
        }
    }
}