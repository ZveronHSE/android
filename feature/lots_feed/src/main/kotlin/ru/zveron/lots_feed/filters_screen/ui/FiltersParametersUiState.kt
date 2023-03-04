package ru.zveron.lots_feed.filters_screen.ui

import androidx.compose.runtime.Immutable

@Immutable
sealed class FiltersParametersUiState {
    @Immutable
    object Loading: FiltersParametersUiState()

    @Immutable
    data class Success(
        val parameters: List<ParameterUiState>,
    ): FiltersParametersUiState()
}

@Immutable
data class ParameterUiState(
    val id: Int,
    val title: String,
    val isUnderlined: Boolean,
)