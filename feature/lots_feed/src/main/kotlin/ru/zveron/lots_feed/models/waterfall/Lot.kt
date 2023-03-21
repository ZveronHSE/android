package ru.zveron.lots_feed.models.waterfall

data class Lot(
    val id: Long,
    val title: String,
    val price: String,
    val publicationDate: String,
    val photoId: Long,
    val isFavorite: Boolean,
)
