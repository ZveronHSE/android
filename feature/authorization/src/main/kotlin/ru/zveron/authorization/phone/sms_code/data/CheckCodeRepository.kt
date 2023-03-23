package ru.zveron.authorization.phone.sms_code.data

import ru.zveron.authorization.model.Token
import ru.zveron.contract.auth.external.PhoneLoginVerifyRequest
import ru.zveron.contract.auth.external.PhoneLoginVerifyResponse
import ru.zveron.contract.auth.external.phoneLoginVerifyRequest
import ru.zveron.network.ApigatewayDelegate

private const val PHONE_LOGIN_VERIFY_METHOD_NAME = "authPhoneLoginVerify"

class CheckCodeRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun sendCode(
        sessionId: String,
        code: String,
        fingerprint: String,
    ): CheckCodeResult {
        val phoneLoginVerifyRequest = phoneLoginVerifyRequest {
            this.sessionId = sessionId
            this.code = code
            this.deviceFp = fingerprint
        }

        val response = apigatewayDelegate.callApiGateway<PhoneLoginVerifyRequest, PhoneLoginVerifyResponse>(
            PHONE_LOGIN_VERIFY_METHOD_NAME,
            phoneLoginVerifyRequest,
            PhoneLoginVerifyResponse.newBuilder(),
        )

        return if (response.hasMobileToken()) {
            val accessToken = Token(
                response.mobileToken.accessToken.token,
                response.mobileToken.accessToken.expiration.seconds,
            )

            val refreshToken = Token(
                response.mobileToken.refreshToken.token,
                response.mobileToken.refreshToken.expiration.seconds,
            )
            CheckCodeResult.Ready(accessToken, refreshToken)
        } else {
            CheckCodeResult.NeedRegister(response.sessionId)
        }
    }
}