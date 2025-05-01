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
    private val _uiState: MutableStateFlow<QueryUiState> = MutableStateFlow(QueryUiState.Loading)
    val uiState: StateFlow<QueryUiState> = _uiState.asStateFlow()

    private val _selectedBookId = MutableStateFlow<String?>(null)
    var selectedBookId: StateFlow<String?> = _selectedBookId.asStateFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _favorites: MutableStateFlow<Set<Book>> = MutableStateFlow(emptySet())
    val favorites: StateFlow<Set<Book>> = _favorites.asStateFlow()

    /** Update query string. */
    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    /** Performs search; updates UI state. */
    fun searchBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = QueryUiState.Loading
            val result = runCatching { bookshelfRepository.getBooks(_searchQuery.value) }
                .getOrElse { e ->
                    _uiState.value = QueryUiState.Error(e.message)
                    return@launch
                }
            _uiState.value = QueryUiState.Success(result)
        }
    }

    /** Toggle favorite status for a book. */
    fun toggleFavorite(book: Book) {
        _favorites.update { current ->
            if (current.any { it.id == book.id }) current - book else current + book
        }
    }
    /** Factory for creating ViewModel with DI. */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                QueryViewModel(bookshelfRepository = bookshelfRepository)
            }
        }
    }
}