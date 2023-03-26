package ru.zveron.models.lots

data class Lot(
    val id: Long,
    val title: String,
    val price: String,
    val publicationDate: String,
    // TODO: Add photo link
    val isFavorite: Boolean,
    val status: Status,
    val categoryId: Int?,
)