package ru.zveron.authorization.phone.phone_input.data

import ru.zveron.authorization.phone.formatting.PhoneFormatter
import ru.zveron.contract.auth.external.PhoneLoginInitRequest
import ru.zveron.contract.auth.external.PhoneLoginInitResponse
import ru.zveron.contract.auth.external.phoneLoginInitRequest
import ru.zveron.network.ApigatewayDelegate

private const val PHONE_LOGIN_INIT_METHOD_NAME = "authPhoneLoginInit"

class PhoneSendRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
    private val phoneFormatter: PhoneFormatter,
) {
    suspend fun sendPhone(phoneInput: String, fingerprint: String): String {
        val actualNumber = phoneFormatter.formatPhoneInputToRequest(phoneInput)

        val phoneInitRequest = phoneLoginInitRequest {
            this.phoneNumber = actualNumber
            this.deviceFp = fingerprint
        }

        val response = apigatewayDelegate.callApiGateway<PhoneLoginInitRequest, PhoneLoginInitResponse>(
            PHONE_LOGIN_INIT_METHOD_NAME,
            phoneInitRequest,
            PhoneLoginInitResponse.newBuilder(),
        )

        return response.sessionId
    }
}