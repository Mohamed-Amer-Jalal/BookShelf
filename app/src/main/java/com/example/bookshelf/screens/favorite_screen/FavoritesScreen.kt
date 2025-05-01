package com.example.bookshelf.screens.favorite_screen

/*
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
}*/
