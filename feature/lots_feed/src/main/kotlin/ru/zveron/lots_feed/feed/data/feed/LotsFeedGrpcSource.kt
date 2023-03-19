package ru.zveron.lots_feed.feed.data.feed

import com.google.protobuf.kotlin.toByteStringUtf8
import com.google.protobuf.util.JsonFormat
import ru.zveron.contract.apigateway.ApigatewayServiceGrpc.ApigatewayServiceBlockingStub
import ru.zveron.contract.apigateway.apiGatewayRequest
import ru.zveron.contract.lot.WaterfallResponse
import ru.zveron.contract.lot.model.parameter
import ru.zveron.contract.lot.waterfallRequest
import ru.zveron.lots_feed.filters_screen.data.parameters.ParameterState
import ru.zveron.lots_feed.mappings.addSortType
import ru.zveron.lots_feed.mappings.toDomainLot
import ru.zveron.lots_feed.mappings.toGrpcFilter
import ru.zveron.lots_feed.models.categories.Category
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.lots_feed.models.waterfall.Lot

private const val WATERFALL_METHOD_NAME = "waterfallGet"

class LotsFeedGrpcSource(
    private val apiGateway: ApigatewayServiceBlockingStub,
): LotsFeedSource {
    override suspend fun loadLots(
        filters: List<Filter>,
        sortType: SortType,
        pageSize: Int,
        parameters: List<ParameterState>?,
        category: Category?,
        query: String?
    ): List<Lot> {
        // TOOD: add all new parameters
        val waterfallRequest = waterfallRequest {
            category?.let {
                this.categoryId = it.id
            }

            parameters?.let { parameterStates ->
                this.parameters.addAll(parameterStates.map {
                    parameter {
                        this.id = it.parameter.id
                        this.name = it.parameter.name
                        this.value = it.value!!
                    }
                })
            }

            query?.let{
                this.query = query
            }

            this.pageSize = pageSize

            this.filters.addAll(filters.map { it.toGrpcFilter() })

            this.addSortType(sortType)
        }

        val request = apiGatewayRequest {
            this.requestBody = JsonFormat.printer().print(waterfallRequest).toByteStringUtf8()
            this.methodAlias = WATERFALL_METHOD_NAME
        }


        val response = apiGateway.callApiGateway(request)

        val responseBuilder = WaterfallResponse.newBuilder()
        JsonFormat.parser().merge(response.responseBody.toStringUtf8(), responseBuilder)

        return responseBuilder
            .build()
            .lotsList
            .map { it.toDomainLot() }
    }
}