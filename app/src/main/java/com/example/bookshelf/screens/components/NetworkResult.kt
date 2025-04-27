package com.example.bookshelf.screens.components

// 1) تعريف عامّ لنتائج الشبكة
sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T)           : NetworkResult<T>()
    data class Error(val exception: Throwable)       : NetworkResult<Nothing>()
    object Loading                                  : NetworkResult<Nothing>()
}