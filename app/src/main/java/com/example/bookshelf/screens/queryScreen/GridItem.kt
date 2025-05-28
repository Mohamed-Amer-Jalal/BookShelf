package com.example.bookshelf.screens.queryScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.screens.components.NothingFoundScreen

@Composable
fun GridList(
    viewModel: SearchViewModel,
    bookshelfList: List<Book>?,
    modifier: Modifier = Modifier,
    onDetailsClick: (Book) -> Unit,
) {
    if (bookshelfList.isNullOrEmpty()) NothingFoundScreen()
    else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(bookshelfList) { book ->
                GridItem(
                    viewModel = viewModel,
                    book = book,
                    onDetailsClick = onDetailsClick
                )
            }
        }
    }
}

@Composable
private fun GridItem(
    viewModel: SearchViewModel,
    book: Book,
    onDetailsClick: (Book) -> Unit,
) {
    // Observe favorites from ViewModel
    var expanded by remember { mutableStateOf(false) }
    var favorite by remember { mutableStateOf(false) }

    Card(
        onClick = { onDetailsClick(book) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.volumeInfo.imageLinks?.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.image_of_book),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                modifier = Modifier.aspectRatio(.6f),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = stringResource(R.string.book_title, book.volumeInfo.title),
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FavoriteButton(
                    isFavorite = favorite,
                    onFavoriteClick = {
                        viewModel.toggleFavorite(book)
                        favorite = !favorite
                    },
                )
                ExpandButton(
                    onClick = { expanded = !expanded },
                    expanded = expanded
                )
            }
            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.book_title, book.volumeInfo.title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(
                            R.string.book_subtitle,
                            book.volumeInfo.subtitle ?: "N/A"
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.book_authors, book.volumeInfo.authorsList),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = stringResource(R.string.book_price, book.price),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteButton(isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = { onFavoriteClick() }
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = stringResource(R.string.favorite_button),
            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ExpandButton(onClick: () -> Unit, expanded: Boolean) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.expand_button) // Providing proper content description
        )
    }
}