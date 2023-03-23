package ru.zveron.authorization.phone.password.data

import com.google.protobuf.kotlin.toByteStringUtf8
import com.google.protobuf.util.JsonFormat.Parser
import ru.zveron.authorization.phone.formatting.PhoneFormatter
import ru.zveron.authorization.model.Token
import ru.zveron.contract.auth.external.MobileToken
import ru.zveron.contract.auth.external.loginByPasswordRequest
import ru.zveron.network.ApigatewayDelegate

private const val LOGIN_WITH_PASSWORD_METHOD_NAME = "TODO: add password method"

class PasswordRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
    private val phoneFormatter: PhoneFormatter,
    private val jsonFormatParser: Parser,
) {
    suspend fun loginWithPassword(phone: String, password: String, fingerprint: String): PasswordLoginResult {
        val actualPhone = phoneFormatter.formatPhoneInputToRequest(phone)

        val passwordRequest = loginByPasswordRequest {
            this.phoneNumber = actualPhone
            this.password = password.toByteStringUtf8()
            this.deviceFp = fingerprint
        }

        val apigatewayResponse = apigatewayDelegate.callApiGateway(
            LOGIN_WITH_PASSWORD_METHOD_NAME,
            passwordRequest
        )

        val responseBuilder = MobileToken.newBuilder()
        jsonFormatParser.merge(apigatewayResponse.responseBody.toStringUtf8(), responseBuilder)

        val response = responseBuilder.build()

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