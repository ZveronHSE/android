package ru.zveron.authorization.phone.phone_input.data

import com.google.protobuf.util.JsonFormat
import ru.zveron.authorization.phone.formatting.PhoneFormatter
import ru.zveron.contract.auth.external.PhoneLoginInitResponse
import ru.zveron.contract.auth.external.phoneLoginInitRequest
import ru.zveron.network.ApigatewayDelegate

private const val PHONE_LOGIN_INIT_METHOD_NAME = "authPhoneLoginInit"

class PhoneSendRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
    private val phoneFormatter: PhoneFormatter,
    private val jsonFormatParser: JsonFormat.Parser,
) {
    suspend fun sendPhone(phoneInput: String, fingerprint: String): String {
        val actualNumber = phoneFormatter.formatPhoneInputToRequest(phoneInput)

        val phoneInitRequest = phoneLoginInitRequest {
            this.phoneNumber = actualNumber
            this.deviceFp = fingerprint
        }

        val apigatewayResponse = apigatewayDelegate.callApiGateway(
            PHONE_LOGIN_INIT_METHOD_NAME,
            phoneInitRequest
        )

        val responseBuilder = PhoneLoginInitResponse.newBuilder()
        jsonFormatParser.merge(apigatewayResponse.responseBody.toStringUtf8(), responseBuilder)
        val response = responseBuilder.build()

        return response.sessionId
    }
}