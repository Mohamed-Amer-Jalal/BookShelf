package com.example.bookshelf.screens.queryScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
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


class QueryViewModel(
    private val bookshelfRepository: BookshelfRepository
) : ViewModel() {
    // Logic for Favorite books -- Beg
    // 1. المصدر الوحيد للحقيقة لقائمة المفضلات
    private val _favoriteBooks = mutableStateListOf<Book>()

    // إذا احتجتِ عرض القائمة، استعملي هذا
    val favoriteBooks: List<Book> get() = _favoriteBooks

    // 2. حالة الواجهة مُشتقة مباشرةً
    //    (يمكنك عرض Success(emptyList()) كحالة "فارغة")
    val favoritesUiState: State<QueryUiState> = derivedStateOf {
        // هنا نفترض أن نجاح الطلب يعني عرض القائمة حتى لو كانت فارغة
        QueryUiState.Success(_favoriteBooks.toList())
    }

    // 3. دالة للتحقّق من وجود الكتاب
    fun isBookFavorite(book: Book): Boolean = _favoriteBooks.any { it.id == book.id }

    // 4. دالة للتبديل بين الإضافة والحذف
    fun toggleFavorite(book: Book) {
        if (isBookFavorite(book)) _favoriteBooks.removeAll { it.id == book.id }
        else _favoriteBooks += book
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