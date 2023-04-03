package ru.zveron.authorization.socials_sheet.data

import ru.zveron.authorization.model.AuthProvider
import ru.zveron.authorization.model.Token
import ru.zveron.authorization.model.toGrpcModel
import ru.zveron.contract.auth.external.LoginBySocialRequest
import ru.zveron.contract.auth.external.MobileToken
import ru.zveron.contract.auth.external.loginBySocialRequest
import ru.zveron.network.ApigatewayDelegate

private const val AUTH_BY_SOCIALS_METHOD_NAME = "authLoginBySocialMedia"

class LoginBySocialsRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun loginBySocial(
        token: String,
        authProvider: AuthProvider,
        fingerprint: String,
    ): LoginBySocialResult {
        val request = loginBySocialRequest {
            this.accessToken = token
            this.authProvider = authProvider.toGrpcModel()
            this.deviceFp = fingerprint
        }

        val response = apigatewayDelegate.callApiGateway<LoginBySocialRequest, MobileToken>(
            AUTH_BY_SOCIALS_METHOD_NAME,
            request,
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

        return LoginBySocialResult(
            accessToken,
            refreshToken,
        )
    }
}