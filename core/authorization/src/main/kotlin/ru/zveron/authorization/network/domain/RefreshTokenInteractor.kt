package ru.zveron.authorization.network.domain

import ru.zveron.authorization.network.data.RefreshTokenRepository
import ru.zveron.authorization.storage.AuthorizationStorage

class RefreshTokenInteractor(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val authorizationStorage: AuthorizationStorage,
) {
    fun refreshToken() {
        val fingerprint = authorizationStorage.deviceFingerPrint ?: return

        val refreshToken = authorizationStorage.refreshToken ?: return

        refreshTokenRepository.refreshToken(refreshToken, fingerprint)
    }
}