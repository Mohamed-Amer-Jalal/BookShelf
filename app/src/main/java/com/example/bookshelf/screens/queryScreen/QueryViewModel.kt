package com.example.bookshelf.screens.queryScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.data.safeApiCall
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.QueryResponse
import com.example.bookshelf.screens.components.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class QueryViewModel(
    private val bookshelfRepository: BookshelfRepository
) : ViewModel() {
//    private val _uiState = MutableStateFlow<QueryUiState>(QueryUiState.Loading)
//    val uiState: StateFlow<QueryUiState> = _uiState.asStateFlow()

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

//    fun updateQuery(query: String) {
//        _uiStateSearch.value = _uiStateSearch.value.copy(query = query)
//    }

    private fun updateSearchStarted(searchStarted: Boolean) {
        _uiStateSearch.value = _uiStateSearch.value.copy(searchStarted = searchStarted)
    }

    fun getBooks(query: String = "") {
        updateSearchStarted(true)
        viewModelScope.launch {

        }
    }

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<QueryUiState> = _query
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { searchResultsFlow(it) }  // → Flow<NetworkResult<List<Book>>>
        .map { it.toUiState() }                   // now `it` is NetworkResult<List<Book>>
        .stateIn(viewModelScope, SharingStarted.Lazily, QueryUiState.Loading)

    private fun searchResultsFlow(query: String): Flow<NetworkResult<List<Book>>> = flow {
        emit(NetworkResult.Loading) // قبل طلب الشبكة

        // 1. اطلب الاستجابة الأصلية من الـ API (فقط الـ Retrofit call)
        val apiResult: NetworkResult<QueryResponse> = safeApiCall {
            bookshelfRepository.getBooks(query)
        }

        // 2. حرّكها إلى NetworkResult<List<Book>>
        val listResult: NetworkResult<List<Book>> = when (apiResult) {
            is NetworkResult.Success -> {
                // apiResult.data هو QueryResponse
                NetworkResult.Success(apiResult.data.items.orEmpty())
            }
            is NetworkResult.Error -> {
                NetworkResult.Error(apiResult.exception)
            }
            NetworkResult.Loading -> {
                NetworkResult.Loading
            }
        }

        // 3. أطلق النتيجة النهائية
        emit(listResult)
    }
}