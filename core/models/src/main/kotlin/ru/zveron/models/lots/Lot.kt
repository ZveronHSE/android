package ru.zveron.models.lots

data class Lot(
    val id: Long,
    val title: String,
    val price: String,
    val publicationDate: String,
    val photoUrl: String,
    val isFavorite: Boolean,
    val status: Status,
    val categoryId: Int?,
)