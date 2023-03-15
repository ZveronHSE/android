package ru.zveron.lots_feed.lot_forms.data

import com.google.protobuf.int32Value
import com.google.protobuf.util.JsonFormat
import ru.zveron.contract.apigateway.ApigatewayServiceGrpc.ApigatewayServiceBlockingStub
import ru.zveron.contract.apigateway.apiGatewayRequest
import ru.zveron.contract.parameter.external.LotFormResponse
import ru.zveron.lots_feed.mappings.toDomainModel
import ru.zveron.lots_feed.models.lot_form.LotForm

private const val GET_LOT_FORMS = "get_lot_forms"

class LotFormGrpcSource(
    private val apigateway: ApigatewayServiceBlockingStub
): LotFormSource {
    override suspend fun loadLotForms(categoryId: Int): List<LotForm> {
        val requestBody = int32Value { this.value = categoryId }
        val request = apiGatewayRequest {
            this.methodAlias = GET_LOT_FORMS
            this.requestBody = requestBody.toByteString()
        }

        val response = apigateway.callApiGateway(request)

        val responseBuilder = LotFormResponse.newBuilder()
        JsonFormat.parser().merge(response.responseBody.toStringUtf8(), responseBuilder)

        return responseBuilder.build()
            .lotFormsList
            .map { it.toDomainModel() }
    }
}