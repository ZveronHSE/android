package ru.zveron.lots_feed.feed.ui.parameters

sealed class ParametersUiState {
    object Hidden: ParametersUiState()

    object Loading: ParametersUiState()

    data class Success(val parameters: List<ParameterUiState>): ParametersUiState()
}

data class ParameterUiState(
    val id: Int,
    val title: String,
    val isActive: Boolean,
)