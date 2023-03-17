package ru.zveron.lots_feed.parameters.data

import com.google.protobuf.kotlin.toByteStringUtf8
import com.google.protobuf.util.JsonFormat
import ru.zveron.contract.apigateway.ApigatewayServiceGrpc.ApigatewayServiceBlockingStub
import ru.zveron.contract.apigateway.apiGatewayRequest
import ru.zveron.contract.parameter.external.parameterRequest
import ru.zveron.contract.parameter.model.ParameterResponse
import ru.zveron.lots_feed.mappings.toDomainModel
import ru.zveron.lots_feed.models.parameters.Parameter

private const val PARAMETER_METHOD_NAME = "parametersGet"

class ParametersGrpcSource(
    private val apigateway: ApigatewayServiceBlockingStub
): ParametersSource {
    override suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter> {
        val parametersRequest = parameterRequest {
            this.categoryId = categoryId
            this.lotFormId = lotFormId
        }

        val request = apiGatewayRequest {
            this.requestBody = JsonFormat.printer().print(parametersRequest).toByteStringUtf8()
            this.methodAlias = PARAMETER_METHOD_NAME
        }

        val response = apigateway.callApiGateway(request)

        val responseBuilder = ParameterResponse.newBuilder()
        JsonFormat.parser().merge(response.responseBody.toStringUtf8(), responseBuilder)

        return responseBuilder
            .build()
            .parametersList
            .map { it.toDomainModel() }
    }
}