package com.example.bookshelf.screens.queryScreen

import android.view.KeyEvent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
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
fun QueryScreen(
    viewModel: QueryViewModel,
    onDetailsClick: (Book) -> Unit,
    onRetry: () -> Unit,
) {
    // 1. جمع قيمة الاستعلام من StateFlow في ViewModel
    val query by viewModel.query.collectAsState()

    // 2. جمع uiState أيضاً لإظهار Loading/Success/Error
    val uiState by viewModel.uiState.collectAsState()

    // 3. الحصول على مدير التركيز لإخفاء لوحة المفاتيح
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = viewModel::updateQuery,
        placeholder = { Text(stringResource(R.string.search)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            uiState
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp)
            .onKeyEvent { event ->
                if (event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    focusManager.clearFocus()
                    uiState
                }
                false
            },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    )

    Spacer(modifier = Modifier.height(16.dp))

    when (uiState) {
        is QueryUiState.Loading -> LoadingScreen()
        is QueryUiState.Success -> {
            val books = (uiState as QueryUiState.Success).bookshelfList
            GridList(
                bookshelfList = books,
                onBookClick = onDetailsClick,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp)
            )
        }
        is QueryUiState.Error -> ErrorScreen(onRetry)
    }
}