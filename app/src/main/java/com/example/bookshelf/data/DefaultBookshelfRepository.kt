package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookshelfApiService
import com.example.bookshelf.screens.components.NetworkResult

class DefaultBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookshelfRepository {

    override suspend fun getBooks(query: String): List<Book> {
        return when (val result = safeApiCall { bookshelfApiService.getBooks(query) }) {
            is NetworkResult.Success -> result.data.items ?: emptyList()
            is NetworkResult.Error -> emptyList()
            is NetworkResult.Loading -> emptyList()
        }
    }

    override suspend fun getBook(id: String): Book? {
        return when (val result = safeApiCall { bookshelfApiService.getBook(id) }) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> null
            is NetworkResult.Loading -> null
        }
    }
}