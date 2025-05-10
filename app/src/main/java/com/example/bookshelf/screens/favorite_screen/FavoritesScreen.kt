package com.example.bookshelf.screens.favorite_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.bookshelf.R
import com.example.bookshelf.screens.queryScreen.GridList
import com.example.bookshelf.screens.queryScreen.QueryViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: QueryViewModel
) {
    val favoriteBooks = viewModel.favoriteBooks
    if (favoriteBooks.isNotEmpty()) GridList(
        viewModel = viewModel,
        bookshelfList = favoriteBooks,
        modifier = modifier,
        onDetailsClick = { }
    )
    else Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            stringResource(R.string.NoFavoriteBooksText)
        )
    }
}
