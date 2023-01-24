package ru.zveron.authorization.storage

interface AuthorizationStorage {
    fun isAuthorizaed(): Boolean

    val accessToken: String?

    val refreshToken: String?

    val deviceFingerPrint: String?

    fun refreshTokens(accessToken: String, refreshToken: String)

    fun updateAccessToken(accessToken: String, expiresIn: Long?)

    fun updateRefreshToken(refreshToken: String, expiresIn: Long?)

    fun refreshDeviceFingerprint(fingerprint: String)
}