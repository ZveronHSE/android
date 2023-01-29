package ru.zveron.authorization.phone.registration.data

import java.io.IOException

class RegistrationRepository(
    private val registrationApi: RegistrationApi,
) {
    suspend fun register(
        phone: String,
        password: String,
        name: String,
        fingerPrint: String
    ): Boolean {
        val request = RegistrationRequest(
            phone = phone,
            password = password,
            name = name,
            surname = "qq",
            fingerPrint = fingerPrint,
        )

        return try {
            val response = registrationApi.register(request)
            response.isSuccessful
        } catch (e: IOException) {
            false
        }
    }
}