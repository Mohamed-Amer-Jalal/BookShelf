package com.example.bookshelf.di

import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.network.KtorBookshelfService

interface AppContainer {
    val bookshelfApiService: KtorBookshelfService
    val bookshelfRepository: BooksRepository
}