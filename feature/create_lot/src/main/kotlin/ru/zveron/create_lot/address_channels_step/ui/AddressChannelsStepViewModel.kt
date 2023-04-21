package ru.zveron.create_lot.address_channels_step.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zveron.create_lot.address_channels_step.AddressChannelsStepNavigator
import ru.zveron.create_lot.data.CreateLotRepository
import ru.zveron.create_lot.data.LotCreateAddressRepository
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.models.communication_channels.CommunicationChannel

internal class AddressChannelsStepViewModel(
    addressRepository: LotCreateAddressRepository,
    private val lotCreateInfoRepository: LotCreateInfoRepository,
    private val createLotRepository: CreateLotRepository,
    private val navigator: AddressChannelsStepNavigator,
) : ViewModel() {
    private val _selectedCommunicationChannels =
        MutableStateFlow<Map<CommunicationChannelUiState, Boolean>>(emptyMap())

    private val _continueButtonLoading = MutableStateFlow(false)

    val addressInputState = mutableStateOf("")

    val uiState = combine(
//        addressRepository.addressState,
        snapshotFlow { addressInputState.value },
        _selectedCommunicationChannels,
        _continueButtonLoading,
    ) { addressState, communicationChannelsState, loading ->
        val continueButtonEnabled = addressState.isNotBlank() && communicationChannelsState.any { it.value }
//        val continueButtonEnabled = addressState != null && communicationChannelsState.any { it.value }

        AddressChannelsStepUiState(
//            addressState?.title,
            addressState,
            CommunicationChannelUiState.values().map {
                val selected = communicationChannelsState[it] ?: false
                selected to it
            },
            ContinueButtonState(continueButtonEnabled, loading),
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, AddressChannelsStepUiState(
        null,
        CommunicationChannelUiState.values().map { false to it },
        ContinueButtonState(enabled = false, isLoading = false),
    ))

    fun addressClicked() {

    }

    fun communicationChannelClicked(channel: CommunicationChannelUiState) {
        val currentValue = _selectedCommunicationChannels.value[channel] ?: false
        _selectedCommunicationChannels.update { state ->
            buildMap {
                putAll(state)
                put(channel, !currentValue)
            }
        }
    }

    fun setAddress(input: String) {
        addressInputState.value = input
    }

    fun onContinueClicked() {
        lotCreateInfoRepository.setAddress(addressInputState.value)
        val channels = _selectedCommunicationChannels.value
            .map {
                if (!it.value) {
                    null
                } else {
                    when (it.key) {
                        CommunicationChannelUiState.CALL -> CommunicationChannel.CALL
                        CommunicationChannelUiState.CHAT -> CommunicationChannel.CHAT
                    }
                }
            }
            .filterNotNull()
        lotCreateInfoRepository.setCommunicationChannels(channels)

        viewModelScope.launch {
            try {
                _continueButtonLoading.update { true }
                val id = withContext(Dispatchers.IO) {
                    createLotRepository.createLot()
                }
                _continueButtonLoading.update { false }
                navigator.completeAddressChannelsStep(id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalStateException) {
                throw e
            } catch (e: Exception) {
                Log.e("Lot create", "error creating lot", e)
                _continueButtonLoading.update { false }
            }
        }
    }
}