package ru.zveron.authorization.network.data

import com.google.protobuf.kotlin.toByteStringUtf8
import com.google.protobuf.util.JsonFormat.Parser
import com.google.protobuf.util.JsonFormat.Printer
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.contract.apigateway.ApigatewayServiceGrpcKt.ApigatewayServiceCoroutineStub
import ru.zveron.contract.apigateway.apiGatewayRequest
import ru.zveron.contract.auth.external.MobileToken
import ru.zveron.contract.auth.external.accessTokenOrNull
import ru.zveron.contract.auth.external.issueNewTokensRequest
import ru.zveron.contract.auth.external.refreshTokenOrNull

private const val ISSUE_NEW_TOKENS_METHOD_NAME = "authIssueNewTokens"

class RefreshTokenRepository(
    private val apigateway: ApigatewayServiceCoroutineStub,
    private val jsonFormatPrinter: Printer,
    private val jsonFormatParser: Parser,
    private val authorizationStorage: AuthorizationStorage,
) {
    suspend fun refreshToken(refreshToken: String, fingerPrint: String) {
        val refreshRequest = issueNewTokensRequest {
            this.refreshToken = refreshToken
            this.deviceFp = fingerPrint
        }

        val apigatewayRequest = apiGatewayRequest {
            this.methodAlias = ISSUE_NEW_TOKENS_METHOD_NAME
            this.requestBody = jsonFormatPrinter.print(refreshRequest).toByteStringUtf8()
        }

        val response = apigateway.callApiGateway(apigatewayRequest)

        val responseBuilder = MobileToken.newBuilder()
        jsonFormatParser.merge(response.responseBody.toStringUtf8(), responseBuilder)
        val mobileToken = responseBuilder.build()

        mobileToken.accessTokenOrNull?.let {
            authorizationStorage.updateAccessToken(it.token, it.expiration.seconds)
        }
        mobileToken.refreshTokenOrNull?.let {
            authorizationStorage.updateRefreshToken(it.token, it.expiration.seconds)
        }
    }
}