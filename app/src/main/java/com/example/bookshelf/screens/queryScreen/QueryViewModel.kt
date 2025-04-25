package com.example.bookshelf.screens.queryScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueryViewModel : ViewModel() {
    /*private val _uiState = MutableStateFlow<QueryUiState>(QueryUiState.Loading)
    val uiState: StateFlow<QueryUiState> = _uiState.asStateFlow()*/

    private val _uiStateSearch = MutableStateFlow(SearchUiState())
    val uiStateSearch: StateFlow<SearchUiState> = _uiStateSearch.asStateFlow()

    fun updateQuery(query: String) {
        _uiStateSearch.value = _uiStateSearch.value.copy(query = query)
    }

    fun updateSearchStarted(searchStarted: Boolean) {
        _uiStateSearch.value = _uiStateSearch.value.copy(searchStarted = searchStarted)
    }
}