package com.example.bookshelf.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiTetheringErrorRounded
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.WifiTetheringErrorRounded,
            contentDescription = stringResource(R.string.failed_to_load),
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = stringResource(R.string.failed_to_load),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(onClick = retryAction, modifier = Modifier.padding(top = 16.dp)) {
            Text(stringResource(R.string.try_again))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ErrorScreenPreview() {
    ErrorScreen(retryAction = { })
}