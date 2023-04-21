package ru.zveron.lot_card.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.lot_card.domain.Gender

@Immutable
sealed class LotCardUiState {
    object Error: LotCardUiState()

    object Loading: LotCardUiState()

    data class Success(
        val photos: List<ZveronImage>,
        val title: String,
        val address: String,
        val tags: List<LotCardTag>,
        val description: String,
        val sellerId: Long,
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

@Immutable
sealed class CommunicationAction {
    data class Vk(val link: String): CommunicationAction()

    data class PhoneCall(val phone: String): CommunicationAction()

    data class WriteEmail(val email: String): CommunicationAction()

    object Chat: CommunicationAction()
}