package com.example.bookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String,
    val description: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo? = null
) {
    val price: String
        get() = saleInfo?.priceDisplay.orEmpty()
}

@Serializable
data class VolumeInfo(
    val title: String,
    val subtitle: String,
    val description: String,
    val imageLinks: ImageLinks? = null,
    val authors: List<String> = emptyList(),
    val publisher: String,
    val publishedDate: String
) {
    val authorsList: String
        get() = authors.joinToString(", ")
}

@Serializable
data class ImageLinks(
    @SerialName("img_src") val smallThumbnail: String,
    @SerialName("img_src2") val thumbnail: String
) {
    val secureThumbnail: String
        get() = thumbnail.replace("http://", "https://")
}

@Serializable
data class SaleInfo(
    val country: String,
    val isEbook: Boolean,
    val listPrice: ListPrice? = null
) {
    val priceDisplay: String
        get() = listPrice?.let { "${it.amount} ${it.currency}" } ?: "N/A"
}

@Serializable
data class ListPrice(
    val amount: Float,
    val currency: String
)