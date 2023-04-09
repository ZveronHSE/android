package ru.zveron.lot_card.ui

import androidx.compose.ui.graphics.Brush
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.lot_card.domain.Gender

sealed class LotCardUiState {
    object Loading: LotCardUiState()

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
        val communicationButtons: List<CommunicationButton>,
        val views: Int,
        val favorites: Int,
        val gender: Gender,
    ): LotCardUiState()
}

data class CommunicationButton(
    val text: ZveronText,
    val brush: Brush,
    val action: CommunicationAction,
)

sealed class CommunicationAction {
    data class Vk(val link: String): CommunicationAction()

    data class PhoneCall(val phone: String): CommunicationAction()

    data class WriteEmail(val email: String): CommunicationAction()

    object Chat: CommunicationAction()
}