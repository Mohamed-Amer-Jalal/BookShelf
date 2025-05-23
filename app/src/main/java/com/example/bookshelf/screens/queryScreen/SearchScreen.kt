package com.example.bookshelf.screens.queryScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.screens.components.ErrorScreen
import com.example.bookshelf.screens.components.LoadingScreen

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    retryAction: () -> Unit,
    onDetailsClick: (Book) -> Unit,
    onBack: () -> Boolean
) {
    val focusManager = LocalFocusManager.current
    val uiState = viewModel.uiState.collectAsState().value
    val searchState = viewModel.searchState.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchState.search,
            onValueChange = { viewModel.updateSearchState(query = it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.search)) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchState.search.isNotEmpty()) {
                    IconButton(onClick = {
                        viewModel.updateSearchState(query = "")
                        focusManager.clearFocus()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                viewModel.getBooks(searchState.search)
            }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        if (searchState.searchStarted) {
            when (uiState) {
                is QueryUiState.Loading -> LoadingScreen(modifier = modifier)

                is QueryUiState.Success ->
                    GridList(
                        viewModel = viewModel,
                        bookshelfList = uiState.books,
                        modifier = modifier,
                        onDetailsClick = onDetailsClick
                    )

                is QueryUiState.Error -> ErrorScreen(retryAction = retryAction, modifier = modifier)
            }
        }
    }
}