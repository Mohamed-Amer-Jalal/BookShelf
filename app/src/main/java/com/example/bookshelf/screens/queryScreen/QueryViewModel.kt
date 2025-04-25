package com.example.bookshelf.screens.queryScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QueryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<QueryUiState>(QueryUiState.Loading)
    val uiState: StateFlow<QueryUiState> = _uiState.asStateFlow()

    private val _uiStateSearch = MutableStateFlow(SearchUiState())
    val uiStateSearch: StateFlow<SearchUiState> = _uiStateSearch.asStateFlow()

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

    fun updateQuery(query: String) {
        _uiStateSearch.value = _uiStateSearch.value.copy(query = query)
    }

    private fun updateSearchStarted(searchStarted: Boolean) {
        _uiStateSearch.value = _uiStateSearch.value.copy(searchStarted = searchStarted)
    }

    fun getBooks(query: String = "") {
        updateSearchStarted(true)
        viewModelScope.launch {

        }
    }
}