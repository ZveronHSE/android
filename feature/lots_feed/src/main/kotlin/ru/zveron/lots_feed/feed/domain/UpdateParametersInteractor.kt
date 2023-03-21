package ru.zveron.lots_feed.feed.domain

import kotlinx.coroutines.flow.SharedFlow

interface UpdateParametersInteractor {
    val updateFlow: SharedFlow<Unit>

    fun update()

    suspend fun loadParameters()
}