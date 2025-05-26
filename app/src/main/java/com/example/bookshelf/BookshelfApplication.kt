package com.example.bookshelf

import android.app.Application
import com.example.bookshelf.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BookshelfApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookshelfApplication)
            modules(appModules)
        }
    }
}