package com.example.bookshelf.screens.queryScreen

import com.example.bookshelf.model.Book
import com.example.bookshelf.screens.components.NetworkResult

sealed interface QueryUiState {
    data class Success(val bookshelfList: List<Book>) : QueryUiState
    object Error : QueryUiState
    object Loading : QueryUiState
}

fun NetworkResult<List<Book>>.toUiState(): QueryUiState =
    when (this) {
        NetworkResult.Loading -> QueryUiState.Loading
        is NetworkResult.Success -> QueryUiState.Success(data)
        is NetworkResult.Error -> QueryUiState.Error
    }
