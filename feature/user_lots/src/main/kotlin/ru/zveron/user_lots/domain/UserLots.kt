package ru.zveron.user_lots.domain

data class Lot(
    val id: Long,
    val title: String,
    val price: String,
    val imageUrl: String,
    val isActive: Boolean,
)