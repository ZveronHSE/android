package ru.zveron.authorization.phone.sms_code.domain

import ru.zveron.authorization.phone.sms_code.data.CheckCodeRepository
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.authorization.phone.sms_code.data.CheckCodeResult as DataCheckCodeResult

class CheckCodeInteractor(
    private val checkCodeRepository: CheckCodeRepository,
    private val authorizationStorage: AuthorizationStorage,
) {
    suspend fun checkCode(sessionId: String, code: String): CheckCodeResult {
        val fingerprint = authorizationStorage.deviceFingerPrint!!
        val result = checkCodeRepository.sendCode(
            sessionId,
            code,
            fingerprint,
        )

        return when(result) {
            is DataCheckCodeResult.NeedRegister -> {
                CheckCodeResult.NeedRegister(result.sessionId)
            }
            is DataCheckCodeResult.Ready -> {
                authorizationStorage.updateAccessToken(result.accessToken.value, result.accessToken.expiresIn)
                authorizationStorage.updateRefreshToken(result.refreshToken.value, result.refreshToken.expiresIn)

                CheckCodeResult.Ready
            }
        }
    }
}