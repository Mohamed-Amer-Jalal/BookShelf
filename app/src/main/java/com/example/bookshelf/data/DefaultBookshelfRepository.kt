package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookshelfApiService
import com.example.bookshelf.screens.components.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

class DefaultBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookshelfRepository {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> Response<T>
    ): NetworkResult<T> = withContext(dispatcher) {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) NetworkResult.Success(body)
                else NetworkResult.Error(NullPointerException("Response body is null"))
            } else NetworkResult.Error(HttpException(response))
        } catch (e: IOException) {
            NetworkResult.Error(e) // خطأ شبكة
        } catch (e: HttpException) {
            NetworkResult.Error(e) // خطأ HTTP
        } catch (e: Throwable) {
            NetworkResult.Error(e)
        }
    }

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