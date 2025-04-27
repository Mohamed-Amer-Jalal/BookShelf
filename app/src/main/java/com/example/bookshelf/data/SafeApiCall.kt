package com.example.bookshelf.data

import com.example.bookshelf.model.Book
import com.example.bookshelf.model.QueryResponse
import com.example.bookshelf.screens.components.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

// 2) دالة آمنة عامّة معّمول لها "mapper" لتحويل ApiResp → Domain
suspend fun <ApiResp, Domain> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> Response<ApiResp>,
    mapper: (ApiResp) -> Domain
): NetworkResult<Domain> = withContext(dispatcher) {
    try {
        val resp = apiCall()
        if (resp.isSuccessful) {
            val body = resp.body()
            if (body != null) NetworkResult.Success(mapper(body))
            else NetworkResult.Error(NullPointerException("Response body is null"))
        } else NetworkResult.Error(HttpException(resp))
    } catch (e: IOException) {
        NetworkResult.Error(e)
    } catch (e: HttpException) {
        NetworkResult.Error(e)
    } catch (e: Throwable) {
        NetworkResult.Error(e)
    }
}

// 3) امتدادات مساعِدة على NetworkResult لتسهيل الخروج بقيمة افتراضية
inline fun <T, R> NetworkResult<T>.mapResult(transform: (T) -> R): NetworkResult<R> = when (this) {
    NetworkResult.Loading -> NetworkResult.Loading
    is NetworkResult.Success -> NetworkResult.Success(transform(data))
    is NetworkResult.Error -> this
}

fun <T> NetworkResult<T>.getOrDefault(default: T): T = when (this) {
    is NetworkResult.Success -> data
    else -> default
}

fun <T> NetworkResult<T>.getOrNull(): T? = (this as? NetworkResult.Success)?.data

internal fun QueryResponse.toDomainList(): List<Book> = items.orEmpty()