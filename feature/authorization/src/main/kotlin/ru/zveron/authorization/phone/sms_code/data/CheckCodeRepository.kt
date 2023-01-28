package ru.zveron.authorization.phone.sms_code.data

import java.io.IOException

private const val NO_CONTENT_CODE = 204

class CheckCodeRepository(
    private val checkCodeApi: CheckCodeApi,
) {
    suspend fun sendCode(checkCodeRequest: CheckCodeRequest): Boolean {
        return try {
            val response = checkCodeApi.checkCode(checkCodeRequest)
            response.isSuccessful && response.code()  != NO_CONTENT_CODE
        } catch (e: IOException) {
            false
        }
    }
}