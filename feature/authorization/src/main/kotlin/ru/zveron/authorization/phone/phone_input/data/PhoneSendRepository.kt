package ru.zveron.authorization.phone.phone_input.data

import java.io.IOException

class PhoneSendRepository(
    private val api: PhoneSendApi,
) {
    suspend fun sendPhone(phoneInput: String): Boolean {
        return try {
            val response = api.sendPhone(
                PhoneSendRequest(phone = "7$phoneInput")
            )
            response.isSuccessful
        } catch (e: IOException) {
            false
        }
    }
}