package com.example.bookshelf.di

import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.data.KtorBookshelfRepository
import com.example.bookshelf.network.KtorBookshelfService
import com.example.bookshelf.screens.queryScreen.SearchViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


// 1) HttpClient
val networkModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
}

// 2) Service
val serviceModule = module {
    single { KtorBookshelfService(get()) }
}

// 3) Repository
val repositoryModule = module {
    single<BooksRepository> { KtorBookshelfRepository(get()) }
}

// 4) ViewModel
val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
}

// تجميع كل الوحدات
val appModules = listOf(
    networkModule, serviceModule, repositoryModule, viewModelModule
)