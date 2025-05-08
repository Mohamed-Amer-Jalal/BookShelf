package com.example.bookshelf.screens.queryScreen

import com.example.bookshelf.model.Book

sealed interface QueryUiState {
    object Loading : QueryUiState
    data class Success(val books: List<Book>) : QueryUiState
    object Error : QueryUiState
}