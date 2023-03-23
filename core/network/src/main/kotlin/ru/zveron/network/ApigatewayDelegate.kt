package ru.zveron.network

import com.google.protobuf.Message
import com.google.protobuf.MessageOrBuilder

interface ApigatewayDelegate {
    suspend fun <ReqT: MessageOrBuilder, RespT: Message> callApiGateway(
        methodName: String,
        body: ReqT,
        responseBuilder: Message.Builder,
    ): RespT
}