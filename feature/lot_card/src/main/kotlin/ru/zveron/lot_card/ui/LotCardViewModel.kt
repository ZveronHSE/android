package ru.zveron.lot_card.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.callButtonGradient
import ru.zveron.lot_card.LotCardNavigator
import ru.zveron.lot_card.LotCardParams
import ru.zveron.lot_card.domain.CommunicationChannel
import ru.zveron.lot_card.domain.LoadLotInfoInteractor
import ru.zveron.lot_card.domain.LotInfo
import ru.zveron.lot_card.domain.Parameter
import ru.zveron.lots_card.R
import kotlin.math.roundToInt

@SuppressLint("StaticFieldLeak")
class LotCardViewModel(
    private val context: Context,
    private val lotCardParams: LotCardParams,
    private val loadLotInfoInteractor: LoadLotInfoInteractor,
    private val navigator: LotCardNavigator,
) : ViewModel() {
    private val _uiState = MutableStateFlow<LotCardUiState>(LotCardUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var permissionDependentAction: CommunicationAction? = null

    private val _permissionEffectFlow = MutableSharedFlow<String>()
    val permissionEffectFlow = _permissionEffectFlow.asSharedFlow()

    init {
        loadLot()
    }

    fun onActionClicked(action: CommunicationAction, checkPermission: Boolean = true) {
        when (action) {
            is CommunicationAction.PhoneCall -> call(action, checkPermission)
            CommunicationAction.Chat -> TODO()
            is CommunicationAction.Vk -> TODO()
            is CommunicationAction.WriteEmail -> TODO()
        }
    }

    fun onSellerClicked(id: Long) {
        navigator.goToSeller(id)
    }

    private fun call(action: CommunicationAction.PhoneCall, checkPermission: Boolean) {
        if (
            checkPermission
            && ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionDependentAction = action
            viewModelScope.launch {
                _permissionEffectFlow.emit(Manifest.permission.CALL_PHONE)
            }
        } else {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                data = Uri.parse("tel:${action.phone}")
            }
            context.startActivity(intent)
        }
    }

    fun onPermissionResult(granted: Boolean) {
        if (granted) {
            permissionDependentAction?.let {
                onActionClicked(it, false)
            }
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
            sellerId = lotInfo.seller.id,
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