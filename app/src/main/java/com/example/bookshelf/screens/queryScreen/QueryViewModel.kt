package com.example.bookshelf.screens.queryScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class QueryViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<QueryUiState>(QueryUiState.Loading)
    val uiState: StateFlow<QueryUiState> = _uiState.asStateFlow()

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    var selectedBookId: String by mutableStateOf("")
        internal set

    private val _favoriteBooks = mutableStateListOf<Book>()
    val favoriteBooks: List<Book> get() = _favoriteBooks

    var favoritesUiState: QueryUiState by mutableStateOf(QueryUiState.Loading)
        private set

    fun isBookFavorite(book: Book): Boolean = _favoriteBooks.any { it.id == book.id }

    fun toggleFavorite(book: Book) {
        if (isBookFavorite(book)) _favoriteBooks.removeAll { it.id == book.id }
        else _favoriteBooks.add(book)
        updateFavoritesUiState()
    }

    private fun updateFavoritesUiState() {
        favoritesUiState = QueryUiState.Loading
        favoritesUiState = QueryUiState.Success(_favoriteBooks)
    }

    fun updateSearchState(query: String? = null, searchStarted: Boolean? = null) =
        _searchState.update {
            it.copy(
                query = query ?: it.query,
                searchStarted = searchStarted ?: it.searchStarted
            )
        }

    fun getBooks(query: String = "") {
        updateSearchState(searchStarted = true)

        viewModelScope.launch {
            _uiState.value = QueryUiState.Loading

            try {
                booksRepository.getBooks(query).orEmpty().let { books ->
                    _uiState.value =
                        if (books.isEmpty()) QueryUiState.Error
                        else QueryUiState.Success(books)
                }
            } catch (e: IOException) {
                _uiState.value = QueryUiState.Error
                e.printStackTrace()
            } catch (e: HttpException) {
                _uiState.value = QueryUiState.Error
                e.printStackTrace()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                QueryViewModel(booksRepository = bookshelfRepository)
            }
        }
    }
}