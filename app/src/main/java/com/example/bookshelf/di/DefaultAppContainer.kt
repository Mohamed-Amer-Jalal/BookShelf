package com.example.bookshelf.di

import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.data.DefaultBookshelfRepository
import com.example.bookshelf.network.BookshelfApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DefaultAppContainer() : AppContainer {

    override val bookshelfApiService: BookshelfApiService by lazy {
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.googleapis.com/books/v1/").build()
            .create(BookshelfApiService::class.java)
    }
    override val bookshelfRepository: BooksRepository by lazy {
        DefaultBookshelfRepository(bookshelfApiService)
    }
}