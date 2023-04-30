package ru.zveron.authorization.storage

internal class AuthorizationStorageImpl(
    private val authorizationPreferencesWrapper: AuthorizationPreferencesWrapper,
): AuthorizationStorage {
    override fun isAuthorized(): Boolean {
        return accessToken != null && refreshToken != null
    }

    override val accessToken: String?
        get() = authorizationPreferencesWrapper.accessToken
    override val accessTokenExpiration: Long?
        get() = authorizationPreferencesWrapper.accessTokenExpiration
    override val refreshToken: String?
        get() = authorizationPreferencesWrapper.refreshToken
    override val deviceFingerPrint: String?
        get() = authorizationPreferencesWrapper.fingerprint

    override fun refreshTokens(accessToken: String, refreshToken: String) {
        authorizationPreferencesWrapper.accessToken = accessToken
        authorizationPreferencesWrapper.refreshToken = refreshToken
    }

    override fun updateAccessToken(accessToken: String, expiresIn: Long?) {
        authorizationPreferencesWrapper.accessToken = accessToken
        expiresIn?.let {
            authorizationPreferencesWrapper.accessTokenExpiration = expiresIn
        }
    }

    override fun updateRefreshToken(refreshToken: String, expiresIn: Long?) {
        authorizationPreferencesWrapper.refreshToken = refreshToken
        expiresIn?.let {
            authorizationPreferencesWrapper.refreshTokenExpiration = expiresIn
        }
    }

    override fun refreshDeviceFingerprint(fingerprint: String) {
        authorizationPreferencesWrapper.fingerprint = fingerprint
    }
}