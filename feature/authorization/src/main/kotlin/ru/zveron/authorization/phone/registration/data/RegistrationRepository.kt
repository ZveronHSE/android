package ru.zveron.authorization.phone.registration.data

import com.google.protobuf.kotlin.toByteStringUtf8
import com.google.protobuf.util.JsonFormat.Parser
import ru.zveron.authorization.model.Token
import ru.zveron.contract.auth.external.MobileToken
import ru.zveron.contract.auth.external.phoneRegisterRequest
import ru.zveron.network.ApigatewayDelegate

private const val REGISTER_BY_PHONE_METHOD_NAME = "authRegisterByPhone"

class RegistrationRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
    private val jsonFormatParser: Parser,
) {
    suspend fun register(
        sessionId: String,
        password: String,
        name: String,
        surname: String,
        fingerPrint: String
    ): RegistrationResult {
        val registerRequest = phoneRegisterRequest {
            this.sessionId = sessionId
            this.password = password.toByteStringUtf8()
            this.name = name
            this.surname = surname
            this.deviceFp = fingerPrint
        }

        val apigatewayResult = apigatewayDelegate.callApiGateway(
            REGISTER_BY_PHONE_METHOD_NAME,
            registerRequest
        )

        val responseBuilder = MobileToken.newBuilder()
        jsonFormatParser.merge(apigatewayResult.responseBody.toStringUtf8(), responseBuilder)

        val response = responseBuilder.build()

        val accessToken = Token(
            response.accessToken.token,
            response.accessToken.expiration.seconds,
        )

        val refreshToken = Token(
            response.refreshToken.token,
            response.refreshToken.expiration.seconds,
        )

        return RegistrationResult(
            accessToken,
            refreshToken,
        )
    }
}