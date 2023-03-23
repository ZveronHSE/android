package ru.zveron.network

import com.google.protobuf.MessageOrBuilder
import ru.zveron.contract.apigateway.ApigatewayResponse

interface ApigatewayDelegate {
    suspend fun <T: MessageOrBuilder> callApiGateway(
        methodName: String,
        body: T,
    ): ApigatewayResponse
}