package ru.zveron.lots_feed.filters_screen.data.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class FiltersParametersLoadingRepository {
    private val _isLoadingFlow = MutableStateFlow(true)
    val isLoadingFlow = _isLoadingFlow.asStateFlow()

    fun updateIsLoading(isLoading: Boolean) {
        _isLoadingFlow.update { isLoading }
    }
}