package ru.zveron.lots_feed.feed.domain

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class QueryInteractor {
    private val _updateQueryFlow =
        MutableSharedFlow<Pair<String, Boolean>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val updateQueryFlow = _updateQueryFlow.asSharedFlow()

    fun updateQuery(value: String, byUser: Boolean = true) {
        _updateQueryFlow.tryEmit(value to byUser)
    }

    fun resetQuery() {
        _updateQueryFlow.tryEmit("" to false)
    }
}