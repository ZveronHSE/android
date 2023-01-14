package ru.zveron.authorization.storage

interface AuthorizationStorage {
    fun isAuthorizaed(): Boolean

    val accessToken: String?

    val refreshToken: String?

    val deviceFingerPrint: String?

    fun refreshTokens(accessToken: String, refreshToken: String)

    fun refreshDeviceFingerprint(fingerprint: String)
}