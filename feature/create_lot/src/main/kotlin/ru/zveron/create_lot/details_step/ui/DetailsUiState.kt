package ru.zveron.create_lot.details_step.ui

import androidx.compose.runtime.Immutable
import ru.zveron.design.wrappers.ListWrapper

@Immutable
sealed class ParametersUiState {
    object Loading: ParametersUiState()

    data class Success(val parameters: ListWrapper<ParameterUiState>): ParametersUiState()
}

data class ParameterUiState(
    val id: Int,
    val title: String,
    val selected: Boolean,
)

data class ContinueButtonState(val enabled: Boolean)