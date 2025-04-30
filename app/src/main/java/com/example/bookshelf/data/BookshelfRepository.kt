package com.example.bookshelf.data

import com.example.bookshelf.model.Book

// 1) وسّع الواجهة لتعريف الدالة الجديدة
interface BookshelfRepository {
    // الدوال القديمة
    suspend fun getBooks(query: String): List<Book>?

    suspend fun getBook(id: String): Book?
}