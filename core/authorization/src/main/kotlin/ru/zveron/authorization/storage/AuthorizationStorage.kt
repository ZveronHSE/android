package ru.zveron.authorization.storage

interface AuthorizationStorage {
    fun isAuthorized(): Boolean

    val accessToken: String?

    val accessTokenExpiration: Long?


    val refreshToken: String?

    val deviceFingerPrint: String?

    fun refreshTokens(accessToken: String, refreshToken: String)

    fun updateAccessToken(accessToken: String, expiresIn: Long?)

    fun updateRefreshToken(refreshToken: String, expiresIn: Long?)

    fun refreshDeviceFingerprint(fingerprint: String)

    fun clearAuthorization()
}