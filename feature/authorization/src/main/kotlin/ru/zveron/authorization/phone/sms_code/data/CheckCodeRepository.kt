package ru.zveron.authorization.phone.sms_code.data

import ru.zveron.authorization.phone.sms_code.domain.CheckCodeResult
import java.io.IOException

private const val NO_CONTENT_CODE = 204

class CheckCodeRepository(
    private val checkCodeApi: CheckCodeApi,
) {
    suspend fun sendCode(checkCodeRequest: CheckCodeRequest): CheckCodeResult? {
        return try {
            val response = checkCodeApi.checkCode(checkCodeRequest)
            if (!response.isSuccessful) {
                null
            } else {
                CheckCodeResult(response.code() == NO_CONTENT_CODE)
            }
        } catch (e: IOException) {
            null
        }
    }
}