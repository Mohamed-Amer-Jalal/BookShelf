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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@Composable
fun QueryScreen(query: String, onQueryChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(stringResource(R.string.search)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp)
            .onKeyEvent { event ->
                if (event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    focusManager.clearFocus()
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
}

@Preview(showBackground = true)
@Composable
fun QueryScreenPreview() {
    QueryScreen(
        query = "",
        onQueryChange = {}
    )
}