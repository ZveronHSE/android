package ru.zveron.network

import com.google.protobuf.Message
import com.google.protobuf.MessageOrBuilder
import com.google.protobuf.kotlin.toByteStringUtf8
import com.google.protobuf.util.JsonFormat
import io.grpc.Metadata
import io.grpc.Status
import ru.zveron.authorization.network.domain.RefreshTokenInteractor
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.contract.apigateway.ApigatewayResponse
import ru.zveron.contract.apigateway.ApigatewayServiceGrpcKt.ApigatewayServiceCoroutineStub
import ru.zveron.contract.apigateway.apiGatewayRequest

internal class ApigatewayDelegateImpl(
    private val apigatewayServiceCoroutineStub: ApigatewayServiceCoroutineStub,
    private val authorizationStorage: AuthorizationStorage,
    private val refreshTokenInteractor: RefreshTokenInteractor,
    private val jsonFormatPrinter: JsonFormat.Printer,
    private val jsonFormatParser: JsonFormat.Parser,
) : ApigatewayDelegate {
    private val accessTokenKey = Metadata.Key.of("access_token", Metadata.ASCII_STRING_MARSHALLER)

    override suspend fun <ReqT : MessageOrBuilder, RespT : Message> callApiGateway(
        methodName: String,
        body: ReqT,
        responseBuilder: Message.Builder,
    ): RespT {
        val requestBody = jsonFormatPrinter.print(body).toByteStringUtf8()

        val request = apiGatewayRequest {
            this.methodAlias = methodName
            this.requestBody = requestBody
        }

        return try {
            val apigatewayResponse =
                apigatewayServiceCoroutineStub.callApiGateway(request, buildMetadata())
            processResponse(apigatewayResponse, responseBuilder)
        } catch (e: io.grpc.StatusException) {
            if (e.status.code != Status.Code.UNAUTHENTICATED) {
                throw e
            }
            refreshTokenInteractor.refreshToken()
            processResponse(
                apigatewayServiceCoroutineStub.callApiGateway(request, buildMetadata()),
                responseBuilder
            )
        }
    }

    private fun buildMetadata(): Metadata {
        return Metadata().apply {
            authorizationStorage.accessToken?.let {
                this.put(accessTokenKey, it)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> processResponse(
        apigatewayResponse: ApigatewayResponse,
        builder: Message.Builder,
    ): T {
        jsonFormatParser.merge(apigatewayResponse.responseBody.toStringUtf8(), builder)
        return builder.build() as T
    }
}