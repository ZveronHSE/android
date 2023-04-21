package ru.zveron.create_lot.address_channels_step.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import ru.zveron.create_lot.R

@Immutable
data class AddressChannelsStepUiState(
    val addressTitle: String?,
    val channelsState: List<Pair<Boolean, CommunicationChannelUiState>>,
    val continueButtonState: ContinueButtonState,
)

enum class CommunicationChannelUiState(@StringRes val resId: Int) {
    CALL(R.string.call_communication_channel),
    CHAT(R.string.chat_communication_channel),
}

data class ContinueButtonState(
    val enabled: Boolean,
    val isLoading: Boolean,
)

