package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.screens.queryScreen.QueryUiState
import kotlinx.coroutines.flow.Flow

// 1) وسّع الواجهة لتعريف الدالة الجديدة
interface BookshelfRepository {
    // الدوال القديمة
    suspend fun getBooks(query: String): List<Book>
    suspend fun getBook(id: String): Book?

    // الدالة الجديدة التي ترجع Flow بحالات واجهة البحث
    fun searchBooksFlow(query: String): Flow<QueryUiState>
}