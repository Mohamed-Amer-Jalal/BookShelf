package com.example.bookshelf.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.bookshelf.NavigationItem
import com.example.bookshelf.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    canNavigateBack: Boolean,
    onNavigateUpClicked: () -> Unit,
    currentScreen: NavigationItem
) {
    TopAppBar(
        title = { Text(text = currentScreen.route) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUpClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.try_again)
                    )
                }
            }
        }
    )
}