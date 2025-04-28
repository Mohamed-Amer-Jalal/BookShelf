package com.example.bookshelf.screens.queryScreen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.Book

@Composable
fun GridList(
    modifier: Modifier = Modifier,
    viewModel: QueryViewModel = viewModel(),
    bookshelfList: List<Book>,
    onBookClick: (Book) -> Unit,
    contentPadding: PaddingValues = PaddingValues(24.dp)
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = contentPadding
    ) {
        items(bookshelfList) { book ->
            GridItem(
                viewModel = viewModel,
                book = book,
                onBookClick = onBookClick,
            )
        }
    }
}

@Composable
private fun GridItem(
    viewModel: QueryViewModel,
    book: Book,
    onBookClick: (Book) -> Unit,
) {
    // حالة التوسع والتمييز كـ mutable state
    var expanded by remember { mutableStateOf(false) }
    // استخدام متغير favorite يتم تحديثه بناءً على حالة الكتاب في قائمة المفضلة
    var favorite by remember { mutableStateOf(viewModel.isBookFavorite(book)) }

    // تسجيل حجم المفضلة لمراقبة التغييرات
    Log.d(TAG, "عدد الكتب المفضلة: ${viewModel.favoriteBooks.size}")

    Card(
        onClick = { onBookClick(book) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // عرض صورة الغلاف للكتاب
            BookCover(book = book)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FavoriteButton(isFavorite = favorite, onFavoriteClick = {
                    viewModel.toggleFavorite(book)
                    favorite = viewModel.isBookFavorite(book)
                })
                ExpandButton(onExpandToggle = { expanded = !expanded }, isExpanded = expanded)
            }
            if (expanded) {
                Column {
                    Text(
                        text = stringResource(R.string.book_title, book.volumeInfo.title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(R.string.book_subtitle, book.volumeInfo.subtitle),
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
fun BookCover(book: Book) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(book.volumeInfo.imageLinks.thumbnail)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.image_of_book),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is AsyncImagePainter.State.Error -> {
                Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = stringResource(R.string.error_loading_image)
                    )
                }
            }

            else -> SubcomposeAsyncImageContent()
        }
    }
}

@Composable
fun FavoriteButton(isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = { onFavoriteClick }
    ) {
        Icon(
            Icons.Default.Favorite,
            contentDescription = stringResource(R.string.favorite_button),
            tint = when (isFavorite) {
                true -> MaterialTheme.colorScheme.primary
                false -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
fun ExpandButton(onExpandToggle: () -> Unit, isExpanded: Boolean) {
    IconButton(onClick = { onExpandToggle }) {
        Icon(
            imageVector = when (isExpanded) {
                true -> Icons.Default.ExpandLess
                false -> Icons.Default.ExpandMore
            },
            contentDescription = stringResource(R.string.expand_button)
        )
    }
}