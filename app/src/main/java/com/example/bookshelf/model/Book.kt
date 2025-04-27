package com.example.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String? = "",
    val description: String? = "",
    val volumeInfo: VolumeInfo? = null,
    val saleInfo: SaleInfo? = null
) {
    val price: String get() = saleInfo?.priceDisplay.orEmpty()
}

@Serializable
data class VolumeInfo(
    val title: String? = "",
    val subtitle: String? = "",
    val description: String? = "",
    val imageLinks: ImageLinks? = null,
    val authors: List<String>? = emptyList(),
    val publisher: String? = "",
    val publishedDate: String? = ""
) {
    val authorsList: String get() = authors.orEmpty().joinToString(", ")
}

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = "",
    val thumbnail: String? = ""
) {
    val secureThumbnail: String get() = thumbnail?.replaceFirst("http://", "https://").orEmpty()
}

@Serializable
data class SaleInfo(
    val country: String = "",
    val isEbook: Boolean = false,
    val listPrice: ListPrice? = null
) {
    val priceDisplay: String
        get() = listPrice?.let { "${it.amount ?: "N/A"} ${it.currency ?: "N/A"}" } ?: "N/A"
}

@Serializable
data class ListPrice(
    val amount: Float? = null,
    val currency: String? = ""
)