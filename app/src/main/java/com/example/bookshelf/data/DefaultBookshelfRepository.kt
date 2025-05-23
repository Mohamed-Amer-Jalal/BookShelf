package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookshelfApiService

/**
 * المستودع الافتراضي لجلب بيانات الكتب من  خدمة الشبكة.
 */
class DefaultBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BooksRepository {
    /**
     * Retrieves a list of books matching the query from the API.
     * Returns an empty list if the request fails or returns no results.
     */
    override suspend fun getBooks(query: String): List<Book> {
        return safeApiCall {
            val response = bookshelfApiService.getBooks(query)
            if (response.isSuccessful) response.body()?.items.orEmpty() else emptyList()
        } ?: emptyList()
    }

    /**
     * Retrieves a specific book by its ID from the API.
     * Returns null if the request fails.
     */
    override suspend fun getBook(id: String): Book? {
        return safeApiCall {
            val response = bookshelfApiService.getBook(id)
            if (response.isSuccessful) response.body() else null
        }
    }

    /**
     * Safe API call handler that executes the call inside a try-catch block.
     * Returns null if an exception occurs.
     */
    private inline fun <T> safeApiCall(apiCall: () -> T): T? {
        return try {
            apiCall()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}