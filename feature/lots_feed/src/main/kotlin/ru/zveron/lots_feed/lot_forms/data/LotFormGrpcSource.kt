package ru.zveron.lots_feed.lot_forms.data

import com.google.protobuf.int32Value
import com.google.protobuf.kotlin.toByteStringUtf8
import com.google.protobuf.util.JsonFormat
import ru.zveron.contract.apigateway.ApigatewayServiceGrpc.ApigatewayServiceBlockingStub
import ru.zveron.contract.apigateway.apiGatewayRequest
import ru.zveron.contract.parameter.external.LotFormResponse
import ru.zveron.lots_feed.mappings.toLotFormDomainModel
import ru.zveron.lots_feed.models.lot_form.LotForm

private const val GET_LOT_FORMS = "lotFormsGet"

class LotFormGrpcSource(
    private val apigateway: ApigatewayServiceBlockingStub
): LotFormSource {
    override suspend fun loadLotForms(categoryId: Int): List<LotForm> {
        val requestBody = int32Value { this.value = categoryId }
        val request = apiGatewayRequest {
            this.requestBody = JsonFormat.printer().print(requestBody).toByteStringUtf8()
            this.methodAlias = GET_LOT_FORMS
        }

        val response = apigateway.callApiGateway(request)

        val responseBuilder = LotFormResponse.newBuilder()
        JsonFormat.parser().merge(response.responseBody.toStringUtf8(), responseBuilder)

        return responseBuilder.build()
            .lotFormsList
            .map { it.toLotFormDomainModel() }
    }
}