package ru.zveron.lot_card.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.callButtonGradient
import ru.zveron.lot_card.LotCardParams
import ru.zveron.lot_card.domain.CommunicationChannel
import ru.zveron.lot_card.domain.LoadLotInfoInteractor
import ru.zveron.lot_card.domain.LotInfo
import ru.zveron.lot_card.domain.Parameter
import ru.zveron.lots_card.R
import kotlin.math.roundToInt

class LotCardViewModel(
    private val lotCardParams: LotCardParams,
    private val loadLotInfoInteractor: LoadLotInfoInteractor,
) : ViewModel() {
    private val _uiState = MutableStateFlow<LotCardUiState>(LotCardUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadLot()
    }

    fun onActionClicked(action: CommunicationAction) {
        when (action) {
            CommunicationAction.Chat -> TODO()
            is CommunicationAction.PhoneCall -> TODO()
            is CommunicationAction.Vk -> TODO()
            is CommunicationAction.WriteEmail -> TODO()
        }
    }

    private fun loadLot() {
        viewModelScope.launch {
            try {
                _uiState.update { LotCardUiState.Loading }
                val lot = loadLotInfoInteractor.loadLot(lotCardParams.id)
                _uiState.update { mapToUiState(lot) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("Lot card", "Error loading lot", e)
            }
        }
    }

    private fun mapToUiState(lotInfo: LotInfo): LotCardUiState.Success {
        return LotCardUiState.Success(
            photos = lotInfo.photos.map { ZveronImage.RemoteImage(it) },
            title = lotInfo.title,
            address = lotInfo.address,
            tags = lotInfo.parameters.map { mapToLotCardTag(it) },
            description = lotInfo.description,
            sellerAvatar = ZveronImage.RemoteImage(lotInfo.seller.avatarUrl),
            sellerName = lotInfo.seller.name,
            rating = lotInfo.seller.rating.roundToInt(),
            maxRating = 5,
            price = lotInfo.price,
            communicationButtons = mapToCommunicationButtons(lotInfo),
            views = lotInfo.statistics.views,
            favorites = lotInfo.statistics.favorites,
            gender = lotInfo.gender,
        )
    }

    private fun mapToLotCardTag(parameter: Parameter): LotCardTag {
        return LotCardTag(title = parameter.name, subtitle = parameter.value)
    }

    private fun mapToCommunicationButtons(lotInfo: LotInfo): List<CommunicationButton> {
        return lotInfo.contact.channels
            .mapNotNull {
                when (it) {
                    CommunicationChannel.PHONE -> CommunicationButton(
                        text = ZveronText.RawResource(R.string.lot_card_call_title),
                        brush = callButtonGradient,
                        action = CommunicationAction.PhoneCall(lotInfo.contact.channelLink.phone!!),
                    )

//                    CommunicationChannel.CHAT -> CommunicationButton(
//                        text = ZveronText.RawResource(R.string.lot_card_chat_title),
//                        brush = enabledButtonGradient,
//                        action = CommunicationAction.Chat,
//                    )

                    CommunicationChannel.CHAT, CommunicationChannel.EMAIL, CommunicationChannel.VK, CommunicationChannel.UNKNOWN -> null
                }
            }
    }
}