package ru.zveron.lots_feed.feed.data

import com.google.protobuf.kotlin.toByteStringUtf8
import com.google.protobuf.util.JsonFormat
import ru.zveron.contract.apigateway.ApigatewayServiceGrpc.ApigatewayServiceBlockingStub
import ru.zveron.contract.apigateway.apiGatewayRequest
import ru.zveron.contract.lot.WaterfallResponse
import ru.zveron.contract.lot.waterfallRequest
import ru.zveron.lots_feed.mappings.addSortType
import ru.zveron.lots_feed.mappings.toDomainLot
import ru.zveron.lots_feed.mappings.toGrpcFilter
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.lots_feed.models.waterfall.Lot

private const val WATERFALL_METHOD_NAME = "waterfallGet"

class LotsFeedGrpcSource(
    private val apiGateway: ApigatewayServiceBlockingStub,
): LotsFeedSource {
    override suspend fun loadLots(filters: List<Filter>, sortType: SortType, pageSize: Int): List<Lot> {
        val waterfallRequest = waterfallRequest {
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