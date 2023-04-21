package ru.zveron.create_lot.address_channels_step.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.zveron.create_lot.data.LotCreateAddressRepository

internal class AddressChannelsStepViewModel(
    addressRepository: LotCreateAddressRepository,
) : ViewModel() {
    private val _selectedCommunicationChannels =
        MutableStateFlow<Map<CommunicationChannelUiState, Boolean>>(emptyMap())

    private val _continueButtonLoading = MutableStateFlow(false)

    val uiState = combine(
        addressRepository.addressState,
        _selectedCommunicationChannels,
        _continueButtonLoading,
    ) { addressState, communicationChannelsState, loading ->
        val continueButtonEnabled = addressState != null && communicationChannelsState.any { it.value }

        AddressChannelsStepUiState(
            addressState?.title,
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
}