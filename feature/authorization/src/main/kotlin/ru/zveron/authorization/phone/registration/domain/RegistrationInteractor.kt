package ru.zveron.authorization.phone.registration.domain

import ru.zveron.authorization.phone.registration.data.RegistrationRepository
import ru.zveron.authorization.storage.AuthorizationStorage

class RegistrationInteractor(
    private val registrationRepository: RegistrationRepository,
    private val authorizationStorage: AuthorizationStorage,
) {
    suspend fun register(
        sessionId: String,
        password: String,
        name: String,
        surname: String,
    ) {
        val fingerprint = authorizationStorage.deviceFingerPrint!!
        val result = registrationRepository.register(
            sessionId,
            password,
            name,
            surname,
            fingerprint
        )

        authorizationStorage.updateAccessToken(result.accessToken.value, result.accessToken.expiresIn)
        authorizationStorage.updateRefreshToken(result.refreshToken.value, result.refreshToken.expiresIn)
    }
}