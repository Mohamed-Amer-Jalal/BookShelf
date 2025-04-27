package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookshelfApiService
import com.example.bookshelf.screens.queryScreen.QueryUiState
import com.example.bookshelf.screens.queryScreen.toQueryUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

// 6) التنفيذ المحسَّن
class DefaultBookshelfRepository(
    private val api: BookshelfApiService
) : BookshelfRepository {

    override suspend fun getBooks(query: String): List<Book> =
        safeApiCall(
            apiCall = { api.getBooks(query) },        // Response<QueryResponse>
            mapper  = { it.items.orEmpty() }          // QueryResponse → List<Book>
        ).getOrDefault(emptyList())

    override suspend fun getBook(id: String): Book? =
        safeApiCall(
            apiCall = { api.getBook(id) },
            mapper = { it }       // هنا الـ Response مباشرةً هو Book
        )
            .getOrNull()

    // التنفيذ الجديد لـ searchBooksFlow
    override fun searchBooksFlow(query: String): Flow<QueryUiState> =
        flow {
            // 1) حالة التحميل
            emit(QueryUiState.Loading)
            // 2) نداء آمن للـ API يحوّل QueryResponse → List<Book>
            val result = safeApiCall(
                apiCall = { api.getBooks(query) },
                mapper = { response -> response.items.orEmpty() }
            )
            // 3) تحويل NetworkResult إلى QueryUiState وإصداره
            emit(result.toQueryUiState())
        }
            .catch {
                // أي خطأ غير متوقع داخل الـ flow
                emit(QueryUiState.Error)
            }
}