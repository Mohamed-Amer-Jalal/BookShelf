package com.example.bookshelf.network

import com.example.bookshelf.model.Book
import com.example.bookshelf.model.QueryResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorBookshelfService(private val client: HttpClient) {
    private val baseUrl = "https://www.googleapis.com/books/v1/volumes"

    suspend fun getBooks(query: String): List<Book> =
        client.get(baseUrl) { parameter("q", query) }.body<QueryResponse>().items.orEmpty()

    suspend fun getBook(id: String): Book? = client.get("$baseUrl/$id").body()
}