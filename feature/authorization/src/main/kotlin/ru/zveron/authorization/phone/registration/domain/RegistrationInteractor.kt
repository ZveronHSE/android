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
    ) {
        val fingerprint = authorizationStorage.deviceFingerPrint!!
        val result = registrationRepository.register(
            sessionId,
            password,
            name,
            // TODO: replace with an actual surname
            "qq",
            fingerprint
        )

        authorizationStorage.updateAccessToken(result.accessToken.value, result.accessToken.expiresIn)
        authorizationStorage.updateAccessToken(result.refreshToken.value, result.refreshToken.expiresIn)
    }
}