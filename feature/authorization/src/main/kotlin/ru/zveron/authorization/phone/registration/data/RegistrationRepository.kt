package ru.zveron.authorization.phone.registration.data

import com.google.protobuf.kotlin.toByteStringUtf8
import ru.zveron.authorization.model.Token
import ru.zveron.contract.auth.external.MobileToken
import ru.zveron.contract.auth.external.PhoneRegisterRequest
import ru.zveron.contract.auth.external.phoneRegisterRequest
import ru.zveron.network.ApigatewayDelegate

private const val REGISTER_BY_PHONE_METHOD_NAME = "authRegisterByPhone"

class RegistrationRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
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

        val response = apigatewayDelegate.callApiGateway<PhoneRegisterRequest, MobileToken>(
            REGISTER_BY_PHONE_METHOD_NAME,
            registerRequest,
            MobileToken.newBuilder(),
        )

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