package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookshelfApiService
import retrofit2.Response

/**
 * المستودع الافتراضي لجلب بيانات الكتب من  خدمة الشبكة.
 */
class DefaultBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BooksRepository {
    /** Retrieves a list of books matching the query from the API */
    override suspend fun getBooks(query: String): List<Book> =
        runCatching {
            bookshelfApiService.getBooks(query).takeIf { it.isSuccessful }?.body()?.items.orEmpty()
        }.getOrElse {
            emptyList()
        }

    /** Retrieves a specific book by ID from the API */
    override suspend fun getBook(id: String): Book? =
        safeApiCall { bookshelfApiService.getBook(id) }

    /**
     * Handles API call and returns the body if successful, null otherwise
     */
    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): T? {
        return try {
            val response = apiCall()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}