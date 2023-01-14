package ru.zveron.authorization.network.domain

import okhttp3.HttpUrl
import ru.zveron.authorization.network.data.RefreshTokenRepository
import ru.zveron.authorization.storage.AuthorizationStorage

class RefreshTokenInteractor(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val authorizationStorage: AuthorizationStorage,
) {
    fun refreshToken(httpUrl: HttpUrl) {
        val fingerprint = authorizationStorage.deviceFingerPrint!!

        val refreshToken = authorizationStorage.refreshToken!!

        val refreshTokenResult =
            refreshTokenRepository.refreshToken(httpUrl, refreshToken, fingerprint)

        authorizationStorage.refreshTokens(
            refreshTokenResult.newAccessToken,
            refreshTokenResult.newRefreshToken
        )
    }
}