package com.example.bookshelf.screens.components

import com.example.bookshelf.model.Book

sealed interface BookUiState {
    data object Loading : BookUiState
    data class ListSuccess(val books: List<Book>) : BookUiState
    data class DetailsSuccess(val book: Book) : BookUiState
    data object Error : BookUiState
}