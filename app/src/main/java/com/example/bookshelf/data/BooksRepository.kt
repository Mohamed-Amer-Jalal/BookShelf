package com.example.bookshelf.data

import com.example.bookshelf.model.Book

interface BooksRepository {
    suspend fun getBooks(query: String): List<Book>?
    suspend fun getBook(id: String): Book?
}