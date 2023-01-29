package ru.zveron.authorization.phone.registration.domain

import ru.zveron.authorization.phone.formatting.PhoneFormatter
import ru.zveron.authorization.phone.registration.data.RegistrationRepository
import ru.zveron.authorization.storage.AuthorizationStorage

class RegistrationInteractor(
    private val registrationRepository: RegistrationRepository,
    private val authorizationStorage: AuthorizationStorage,
    private val phoneFormatter: PhoneFormatter,
) {
    suspend fun register(
        phone: String,
        name: String,
        password: String,
    ): Boolean {
        return authorizationStorage.deviceFingerPrint?.let { fingerPrint ->
            registrationRepository.register(
                phone = phoneFormatter.formatPhoneInputToRequest(phone),
                password = password,
                name = name,
                fingerPrint = fingerPrint,
            )
        } ?: false
    }
}