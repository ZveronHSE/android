package ru.zveron.authorization.fingerprint

import ru.zveron.authorization.storage.AuthorizationStorage
import java.util.UUID

class FingerprintFactory(
    private val authorizationStorage: AuthorizationStorage,
) {
    fun getOrCreateFingerprint(): String {
        val token = authorizationStorage.deviceFingerPrint

        if(token != null) {
            return token
        }

        val newFingerprint = UUID.randomUUID().toString()
        authorizationStorage.refreshDeviceFingerprint(newFingerprint)

        return newFingerprint
    }
}