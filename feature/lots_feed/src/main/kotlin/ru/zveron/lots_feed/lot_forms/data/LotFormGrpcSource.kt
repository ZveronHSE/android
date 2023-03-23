package ru.zveron.lots_feed.lot_forms.data

import com.google.protobuf.Int32Value
import com.google.protobuf.int32Value
import ru.zveron.contract.parameter.external.LotFormResponse
import ru.zveron.lots_feed.mappings.toLotFormDomainModel
import ru.zveron.lots_feed.models.lot_form.LotForm
import ru.zveron.network.ApigatewayDelegate

private const val GET_LOT_FORMS = "lotFormsGet"

class LotFormGrpcSource(
    private val apigateway: ApigatewayDelegate,
): LotFormSource {
    override suspend fun loadLotForms(categoryId: Int): List<LotForm> {
        val requestBody = int32Value { this.value = categoryId }

        val response = apigateway.callApiGateway<Int32Value, LotFormResponse>(GET_LOT_FORMS, requestBody, LotFormResponse.newBuilder())

        return response.lotFormsList.map { it.toLotFormDomainModel() }
    }
}