package com.example.bookshelf.screens.queryScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class QueryViewModel(
    private val bookshelfRepository: BookshelfRepository
) : ViewModel() {
    // Logic for Favorite books -- Beg
    private val _favoriteBooks = mutableStateListOf<Book>()
    val favoriteBooks: List<Book> = _favoriteBooks

    private var _favoritesUiState: QueryUiState by mutableStateOf(QueryUiState.Loading)
    val favoritesUiState: QueryUiState = _favoritesUiState

    fun isFavoriteBook(book: Book): Boolean {
        return !_favoriteBooks.none { it.id == book.id }
    }

    fun addFavoriteBook(book: Book) {
        if (!isFavoriteBook(book)) {
            _favoriteBooks.add(book)
            favoritesUpdated()
        }
    }

    fun removeFavoriteBook(book: Book) {
        _favoriteBooks.removeIf { it.id == book.id }
        favoritesUpdated()
    }

    private fun favoritesUpdated() {
        viewModelScope.launch {
            _favoritesUiState = QueryUiState.Loading
            _favoritesUiState = QueryUiState.Success(favoriteBooks)
        }
    }
    // Logic for Favorite books -- End

    // 1. دفق داخلي mutable لحقل النص
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // 2. استدعاء من الواجهة
    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    // 3. دفق لحالة الواجهة مشتق من _query
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<QueryUiState> =
        _query.debounce(300)                       // تأخير لضغط الطلبات
            .distinctUntilChanged()              // تجاهل نفس النص
            .flatMapLatest { q ->
                bookshelfRepository.searchBooksFlow(q)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = QueryUiState.Loading
            )
}