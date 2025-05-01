package com.example.bookshelf.data

import android.util.Log
import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookshelfApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class DefaultBookshelfRepository(
    private val apiService: BookshelfApiService
) : BookshelfRepository {

    companion object {
        private const val TAG = "BookshelfRepo"
    }

    /**
     * Retrieves list of books; returns empty list on failure.
     */
    override suspend fun getBooks(query: String): List<Book> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBooks(query)
            response.body()?.items.orEmpty()
        } catch (e: IOException) {
            Log.e(TAG, "Error fetching books for query=[$query]", e)
            emptyList()
        }
    }

    /**
     * Retrieves a single book by ID; returns null on failure.
     */
    override suspend fun getBook(id: String): Book? = withContext(Dispatchers.IO) {
        try {
            apiService.getBook(id)
                .body()
        } catch (e: IOException) {
            Log.e(TAG, "Error fetching book id=[$id]", e)
            null
        }
    }
}