package com.example.bookshelf.screens.favorite_screen

import androidx.compose.foundation.layout.Box
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
import com.example.bookshelf.screens.queryScreen.SearchViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    bookshelfList: QueryUiState,
    retryAction: () -> Unit,
    onBack: () -> Boolean
) {
    val hasFavorites = viewModel.favoriteBooks.isNotEmpty()
    if (!hasFavorites) {
        EmptyFavoritesView(modifier)
        return
    }
    when (bookshelfList) {
        is QueryUiState.Loading -> LoadingScreen(modifier = modifier)

        is QueryUiState.Success -> {
            GridList(
                viewModel = viewModel,
                bookshelfList = bookshelfList.books,
                modifier = modifier,
                onDetailsClick = { /* Add navigation or details logic here */ }
            )
        }

        is QueryUiState.Error -> ErrorScreen(retryAction = retryAction)
    }
}

@Composable
private fun EmptyFavoritesView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.NoFavoriteBooksText))
    }
}