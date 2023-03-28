package ru.zveron.authorization.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first

class AuthorizationEventsEmitter {
    private val _authorizationFinished = MutableSharedFlow<Boolean>(replay = 1, extraBufferCapacity = 1)
    val authorizationFinished = _authorizationFinished.asSharedFlow()

    internal fun authorizationFinished(isSuccess: Boolean) {
        _authorizationFinished.tryEmit(isSuccess)
    }

    suspend fun waitForAuthorization(): Boolean {
        return authorizationFinished.first()
    }
}