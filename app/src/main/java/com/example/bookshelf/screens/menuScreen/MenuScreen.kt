package com.example.bookshelf.screens.menuScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.NavigationItem
import com.example.bookshelf.R

//enum class MenuScreenState { Menu, Favorites }
//
//@Composable
//fun MenuScreen(
//    modifier: Modifier = Modifier,
//    viewModel: QueryViewModel = viewModel(factory = QueryViewModel.Factory)
//) {
//    var isSearchActive by remember { mutableStateOf(false) }
//    var currentScreen by remember { mutableStateOf(MenuScreenState.Menu) }
//    val uiState = viewModel.uiState.collectAsState().value
//    val searchState by viewModel.searchState.collectAsState()
//
//    Scaffold(
//        topBar = {
//            MainTopAppBar(
//                isSearchActive = isSearchActive,
//                onSearchToggle = {
//                    isSearchActive = !isSearchActive
//                    currentScreen = MenuScreenState.Menu
//                },
//                onFavoritesClick = { currentScreen = MenuScreenState.Favorites },
//                viewModel = viewModel
//            )
//        }) { innerPadding ->
//        when (currentScreen) {
//            MenuScreenState.Menu -> {
//                if (searchState.searchStarted) {
//                    SearchContent(
//                        uiState = uiState,
//                        modifier = modifier.padding(innerPadding),
//                        viewModel = viewModel
//                    )
//                }
//            }
//
//            MenuScreenState.Favorites -> {
//                FavoritesScreen(
//                    modifier = modifier.padding(innerPadding),
//                    viewModel = viewModel,
//                    bookshelfList = viewModel.uiState.collectAsState().value,
//                    retryAction = { viewModel.getBooks() }
//                )
//            }
//        }
//    }
//}


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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onSearchClick) {
                Text(text = stringResource(R.string.search))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onFavClick) {
                Text(text = stringResource(R.string.favorite))
            }
        }
    }
}

//@Composable
//private fun SearchContent(
//    uiState: QueryUiState,
//    modifier: Modifier = Modifier,
//    viewModel: QueryViewModel
//) {
//    when (uiState) {
//        is QueryUiState.Loading -> LoadingScreen(modifier = modifier)
//
//        is QueryUiState.Success -> {
//            Column(modifier = modifier) {
//                Spacer(modifier = Modifier.height(16.dp))
//                GridList(
//                    contentPadding = PaddingValues(0.dp),
//                    viewModel = viewModel,
//                    bookshelfList = uiState.books,
//                    modifier = Modifier.fillMaxSize(),
//                    onDetailsClick = { viewModel.selectedBookId = it.id }
//                )
//            }
//        }
//
//        is QueryUiState.Error -> ErrorScreen(
//            modifier = modifier,
//            retryAction = { viewModel.getBooks() }
//        )
//    }
//}