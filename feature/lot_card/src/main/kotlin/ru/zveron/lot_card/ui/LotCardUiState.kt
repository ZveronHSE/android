package ru.zveron.lot_card.ui

import ru.zveron.design.resources.ZveronImage

sealed class LotCardUiState{
    data class Success(
        val photos: List<ZveronImage>,
        val title: String,
        val address: String,
        val tags: List<LotCardTag>,
        val description: String,
        val sellerAvatar: ZveronImage,
        val sellerName: String,
        val rating: Int,
        val maxRating: Int,
        val price: String,
    ): LotCardUiState()
}