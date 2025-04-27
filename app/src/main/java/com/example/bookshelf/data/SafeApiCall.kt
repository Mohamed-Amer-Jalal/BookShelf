package com.example.bookshelf.data

import com.example.bookshelf.screens.components.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

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