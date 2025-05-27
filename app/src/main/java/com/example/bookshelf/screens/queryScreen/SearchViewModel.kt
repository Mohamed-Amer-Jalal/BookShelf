package com.example.bookshelf.screens.queryScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.model.Book
import com.example.bookshelf.screens.components.BookUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException

class SearchViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    var selectedBookId: String by mutableStateOf("")
        internal set

    private val _favoriteBooks = mutableStateListOf<Book>()
    val favoriteBooks: List<Book> get() = _favoriteBooks

    var favoritesUiState: BookUiState by mutableStateOf(BookUiState.Loading)
        private set

    fun isBookFavorite(book: Book): Boolean = _favoriteBooks.any { it.id == book.id }

    fun toggleFavorite(book: Book) {
        if (isBookFavorite(book)) _favoriteBooks.removeAll { it.id == book.id }
        else _favoriteBooks.add(book)
        updateFavoritesUiState()
    }

    private fun updateFavoritesUiState() {
        favoritesUiState = BookUiState.Loading
        favoritesUiState = BookUiState.ListSuccess(_favoriteBooks)
    }

    fun updateSearchState(query: String? = null, searchStarted: Boolean? = null) =
        _searchState.update {
            it.copy(
                search = query ?: it.search,
                searchStarted = searchStarted ?: it.searchStarted
            )
        }

    fun getBooks(query: String = "") {
        updateSearchState(searchStarted = true)

        viewModelScope.launch {
            _uiState.value = BookUiState.Loading

            try {
                booksRepository.getBooks(query).orEmpty().let { books ->
                    _uiState.value =
                        if (books.isEmpty()) BookUiState.Error
                        else BookUiState.ListSuccess(books)
                }
            } catch (e: IOException) {
                _uiState.value = BookUiState.Error
                e.printStackTrace()
            } catch (e: HttpException) {
                _uiState.value = BookUiState.Error
                e.printStackTrace()
            }
        }
    }
}