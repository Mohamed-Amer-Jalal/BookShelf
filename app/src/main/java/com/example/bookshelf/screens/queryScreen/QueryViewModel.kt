package com.example.bookshelf.screens.queryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QueryViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    // --- UI State ---
    private val _uiState = MutableStateFlow<QueryUiState>(QueryUiState.Loading)
    val uiState: StateFlow<QueryUiState> = _uiState.asStateFlow()

    // --- Search Query ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // --- Selected Book ---
    private val _selectedBookId = MutableStateFlow<String?>(null)

    // --- Favorites ---
    private val _favorites = MutableStateFlow<Set<Book>>(emptySet())
    val favorites: StateFlow<Set<Book>> = _favorites.asStateFlow()

    // --- Public Actions ---

    /** Update search query text */
    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    /** Set the currently selected book by ID */
    fun setSelectedBookId(bookId: String) {
        _selectedBookId.value = bookId
    }

    /** Toggle favorite status for a book */
    fun toggleFavorite(book: Book) {
        _favorites.update { favorites ->
            if (favorites.any { it.id == book.id }) favorites - book
            else favorites + book
        }
    }

    /** Search books by current query */
    fun searchBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = QueryUiState.Loading
            try {
                val books = bookshelfRepository.getBooks(_searchQuery.value)
                _uiState.value = QueryUiState.Success(books)
            } catch (e: Exception) {
                _uiState.value = QueryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // --- ViewModel Factory ---
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookshelfApplication
                QueryViewModel(application.container.bookshelfRepository)
            }
        }
    }
}