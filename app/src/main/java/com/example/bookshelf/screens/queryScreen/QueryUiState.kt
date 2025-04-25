package com.example.bookshelf.screens.queryScreen

sealed interface QueryUiState {
    data class Success(val bookshelfList: String) : QueryUiState
    object Error : QueryUiState
    object Loading : QueryUiState
}