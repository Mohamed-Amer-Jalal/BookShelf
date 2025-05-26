package com.example.bookshelf.screens.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.screens.components.BookUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class DetailsViewModel(private val bookshelfRepository: BooksRepository) : ViewModel() {
    private val _uiStateDetail = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val uiStateDetail = _uiStateDetail.asStateFlow()

    fun getBook(id: String) {
        viewModelScope.launch {
            _uiStateDetail.value = try {
                // Notes: List<Book>? NULLABLE
                val book = bookshelfRepository.getBook(id)
                if (book == null) BookUiState.Error
                else BookUiState.DetailsSuccess(book)
            } catch (e: IOException) {
                e.printStackTrace()
                BookUiState.Error
            } catch (e: HttpException) {
                e.printStackTrace()
                BookUiState.Error
            }
        }
    }
}