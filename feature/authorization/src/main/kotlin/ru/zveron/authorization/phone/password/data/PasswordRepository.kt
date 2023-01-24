package ru.zveron.authorization.phone.password.data

import ru.zveron.authorization.storage.AuthorizationStorage
import java.io.IOException

class PasswordRepository(
    private val passwordApi: PasswordApi,
    private val authorizationStorage: AuthorizationStorage,
) {
    suspend fun loginWithPassword(phone: String, password: String): Boolean {
        val request = PasswordApiRequest("7$phone", password, authorizationStorage.deviceFingerPrint!!)
        return try {
            val response = passwordApi.passwordAuthorization(request)

            response.isSuccessful
        } catch (e: IOException) {
            false
        }
    }
}