package ru.zveron.lots_feed.parameters.data

import ru.zveron.contract.parameter.external.ParameterRequest
import ru.zveron.contract.parameter.external.parameterRequest
import ru.zveron.contract.parameter.model.ParameterResponse
import ru.zveron.lots_feed.mappings.toDomainModel
import ru.zveron.lots_feed.models.parameters.Parameter
import ru.zveron.network.ApigatewayDelegate

private const val PARAMETER_METHOD_NAME = "parametersGet"

class ParametersGrpcSource(
    private val apigateway: ApigatewayDelegate,
) : ParametersSource {
    override suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter> {
        val parametersRequest = parameterRequest {
            this.categoryId = categoryId
            this.lotFormId = lotFormId
        }

        val response = apigateway.callApiGateway<ParameterRequest, ParameterResponse>(
            PARAMETER_METHOD_NAME,
            parametersRequest,
            ParameterResponse.newBuilder()
        )

        return response.parametersList.map { it.toDomainModel() }
    }
}