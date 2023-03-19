package ru.zveron.lots_feed.feed.data.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ParametersLoadingRepository {
    private val _isLoadingFlow = MutableStateFlow(true)
    val isLoadingFlow = _isLoadingFlow.asStateFlow()

    fun updateIsLoading(isLoading: Boolean) {
        _isLoadingFlow.update { isLoading }
    }
}