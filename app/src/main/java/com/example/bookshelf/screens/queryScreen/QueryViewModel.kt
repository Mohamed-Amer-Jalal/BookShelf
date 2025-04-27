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
import com.example.bookshelf.network.BookshelfApiService
import com.example.bookshelf.screens.components.NetworkResult
import com.example.bookshelf.screens.queryScreen.toUiState
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
    private val bookshelfRepository: BookshelfRepository,
    private val bookshelfApiService: BookshelfApiService
) : ViewModel() {

//    private val _uiState = MutableStateFlow<QueryUiState>(QueryUiState.Loading)
//    val uiState: StateFlow<QueryUiState> = _uiState.asStateFlow()
//
//    private val _uiStateSearch = MutableStateFlow(SearchUiState())
//    val uiStateSearch: StateFlow<SearchUiState> = _uiStateSearch.asStateFlow()

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

/*    fun updateQuery(query: String) {
        _uiStateSearch.value = _uiStateSearch.value.copy(query = query)
    }

    private fun updateSearchStarted(searchStarted: Boolean) {
        _uiStateSearch.value = _uiStateSearch.value.copy(searchStarted = searchStarted)
    }

    fun getBooks(query: String = "") {
        updateSearchStarted(true)
        viewModelScope.launch {

        }
    }*/

    // 1. دفق mutable لحقل النص الذي يكتبه المستخدم
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // 2. تحديث الاستعلام من الواجهة
    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    // 3. تدفق لحالة الواجهة مشتق من تدفق الاستعلام
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<QueryUiState> = _query
        .debounce(300)                               // تأخير لضغط الطلبات أثناء الكتابة
        .distinctUntilChanged()                      // تجاهل نفس النص المتكرر
        .flatMapLatest { q ->                         // إلغاء أي طلب جاري وبناء دفق جديد
            flow {
                // استدعاء المستودع آمنًا
                val result = safeApiCall { bookshelfApiService.getBooks(q) }
                // حوّل النتائج إلى حالة واجهة
                emit(result.toUiState())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QueryUiState.Loading
        )
}