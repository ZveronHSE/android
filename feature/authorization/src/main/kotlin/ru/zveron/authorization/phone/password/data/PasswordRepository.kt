package ru.zveron.authorization.phone.password.data

import com.google.protobuf.kotlin.toByteStringUtf8
import ru.zveron.authorization.phone.formatting.PhoneFormatter
import ru.zveron.authorization.model.Token
import ru.zveron.contract.auth.external.LoginByPasswordRequest
import ru.zveron.contract.auth.external.MobileToken
import ru.zveron.contract.auth.external.loginByPasswordRequest
import ru.zveron.network.ApigatewayDelegate

private const val LOGIN_WITH_PASSWORD_METHOD_NAME = "TODO: add password method"

class PasswordRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
    private val phoneFormatter: PhoneFormatter,
) {
    suspend fun loginWithPassword(phone: String, password: String, fingerprint: String): PasswordLoginResult {
        val actualPhone = phoneFormatter.formatPhoneInputToRequest(phone)

        val passwordRequest = loginByPasswordRequest {
            this.phoneNumber = actualPhone
            this.password = password.toByteStringUtf8()
            this.deviceFp = fingerprint
        }

        val response = apigatewayDelegate.callApiGateway<LoginByPasswordRequest, MobileToken>(
            LOGIN_WITH_PASSWORD_METHOD_NAME,
            passwordRequest,
            MobileToken.newBuilder(),
        )

        val accessToken = Token(
            response.accessToken.token,
            response.accessToken.expiration.seconds
        )

        val refreshToken = Token(
            response.refreshToken.token,
            response.refreshToken.expiration.seconds,
        )

        return PasswordLoginResult(accessToken, refreshToken)
    }
}