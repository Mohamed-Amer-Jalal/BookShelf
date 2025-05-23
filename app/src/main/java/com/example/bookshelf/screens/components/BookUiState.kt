package com.example.bookshelf.screens.components

import com.example.bookshelf.model.Book

sealed interface BookUiState {
    object Loading : BookUiState         // جلب بيانات
    data class ListSuccess(val books: List<Book>) : BookUiState   // عرض قائمة الكتب
    data class DetailsSuccess(val book: Book) : BookUiState       // عرض تفاصيل كتاب واحد
    object Error : BookUiState           // خطأ عام
}