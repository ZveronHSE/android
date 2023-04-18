package ru.zveron.user_lots.domain

data class UserLots(
    val activeLots: List<Lot>,
    val archivedLots: List<Lot>,
)

data class Lot(
    val id: Long,
    val title: String,
    val price: String,
    val imageUrl: String,
    val isActive: Boolean,
    val views: Int,
    val likes: Int,
)