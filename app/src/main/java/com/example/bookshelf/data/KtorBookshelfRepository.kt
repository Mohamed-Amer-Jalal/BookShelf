package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.network.KtorBookshelfService

class KtorBookshelfRepository(private val service: KtorBookshelfService) : BooksRepository {

    override suspend fun getBooks(query: String): List<Book> =
        try {
            service.getBooks(query)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }

    override suspend fun getBook(id: String): Book? =
        try {
            service.getBook(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}