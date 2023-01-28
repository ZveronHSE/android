package ru.zveron.authorization.phone.sms_code.domain

import ru.zveron.authorization.phone.formatting.PhoneFormatter
import ru.zveron.authorization.phone.sms_code.data.CheckCodeRepository
import ru.zveron.authorization.phone.sms_code.data.CheckCodeRequest
import ru.zveron.authorization.storage.AuthorizationStorage

class CheckCodeInteractor(
    private val checkCodeRepository: CheckCodeRepository,
    private val authorizationStorage: AuthorizationStorage,
    private val phoneFormatter: PhoneFormatter,
) {
    suspend fun checkCode(code: String, phone: String): Boolean {
        return authorizationStorage.deviceFingerPrint?.let { fingerprint ->
            val request = CheckCodeRequest(
                phone = phoneFormatter.formatPhoneInputToRequest(phone),
                code = code,
                fingerprint = fingerprint
            )
            return checkCodeRepository.sendCode(request)
        } ?: false
    }
}