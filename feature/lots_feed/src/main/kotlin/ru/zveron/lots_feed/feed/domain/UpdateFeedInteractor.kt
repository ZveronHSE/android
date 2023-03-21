package ru.zveron.lots_feed.feed.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class UpdateFeedInteractor {
    private val _updateFlow = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
    val updateFlow = _updateFlow.asSharedFlow()
    fun update() {
        _updateFlow.tryEmit(Unit)
    }
}