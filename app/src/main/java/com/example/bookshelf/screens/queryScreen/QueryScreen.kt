package com.example.bookshelf.screens.queryScreen

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@Composable
fun QueryScreen(
    viewModel: QueryViewModel,

) {
    val uiStateQuery = viewModel.uiStateSearch.collectAsState().value
    // الحصول على مدير التركيز لإخفاء لوحة المفاتيح
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = uiStateQuery.query,
        onValueChange = { viewModel.updateQuery(it) },
        placeholder = { Text(stringResource(R.string.search)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            viewModel.getBooks(uiStateQuery.query)
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp)
            .onKeyEvent { event ->
                if (event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    focusManager.clearFocus()
                    viewModel.getBooks(uiStateQuery.query)
                }
                false
            },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
    )

    /*if (uiStateQuery.searchStarted) {
        when (uiState) {
            is QueryUiState.Loading -> LoadingScreen(modifier)

            is QueryUiState.Success -> GridList(
                viewModel = viewModel,
                bookshelfList = uiState.bookshelfList,
                modifier = modifier,
                onDetailsClick = onDetailsClick,
            )

            is QueryUiState.Error ->
                ErrorScreen(retryAction = retryAction, modifier)
        }
    }*/
}