package com.example.bookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class Book(
    val id: String,
    val description: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo? = null
) {
    @Transient
    val price: String = saleInfo?.priceDisplay.orEmpty()
}

@Serializable
data class VolumeInfo(
    val title: String,
    val subtitle: String? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null,
    val authors: List<String>? = emptyList(),
    val publisher: String? = null,
    @SerialName("publishedDate")
    val publishedDate: String? = null
) {
    val authorsList: String
        get() = authors.takeIf { it?.isNotEmpty() == true }?.joinToString(", ") ?: "N/A"
}

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
) {
    val secureThumbnail: String? get() = thumbnail?.replace("http://", "https://")
}

@Serializable
data class SaleInfo(
    val country: String,
    @SerialName("isEbook") val isEbook: Boolean,
    val listPrice: ListPrice? = null
) {
    val priceDisplay: String get() = listPrice?.run { "%s %.2f".format(currency, amount) } ?: "N/A"
}

@Serializable
data class ListPrice(
    val amount: Double,
    val currency: String
)