package ru.zveron.authorization.phone.password.domain

import ru.zveron.authorization.phone.password.data.PasswordRepository
import ru.zveron.authorization.storage.AuthorizationStorage

class PasswordLoginInteractor(
    private val passwordLoginRepository: PasswordRepository,
    private val authorizationStorage: AuthorizationStorage,
) {
    suspend fun loginWithPassword(
        phone: String,
        password: String
    ) {
        val fingerprint = authorizationStorage.deviceFingerPrint!!

        val result = passwordLoginRepository.loginWithPassword(phone, password, fingerprint)

        authorizationStorage.updateAccessToken(result.accessToken.value, result.accessToken.expiresIn)
        authorizationStorage.updateRefreshToken(result.refreshToken.value, result.refreshToken.expiresIn)
    }
}