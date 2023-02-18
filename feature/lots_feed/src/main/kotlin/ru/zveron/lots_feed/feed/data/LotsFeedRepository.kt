package ru.zveron.lots_feed.feed.data

import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zveron.contract.apigateway.ApigatewayServiceGrpc.ApigatewayServiceBlockingStub
import ru.zveron.contract.apigateway.apiGatewayRequest
import ru.zveron.contract.lot.Filter
import ru.zveron.contract.lot.SortByDate
import ru.zveron.contract.lot.SortByDateKt
import ru.zveron.contract.lot.SortByDateOrBuilder
import ru.zveron.contract.lot.TypeSort
import ru.zveron.contract.lot.WaterfallRequest
import ru.zveron.contract.lot.WaterfallRequestKt
import ru.zveron.contract.lot.WaterfallResponse
import ru.zveron.contract.lot.sortByDate
import ru.zveron.contract.lot.sortByPrice
import ru.zveron.contract.lot.waterfallRequest
import ru.zveron.lots_feed.feed.ui.SortType

private const val PAGE_SIZE = 32

private const val WATERFALL_METHOD_NAME = "waterfallGet"

class LotsFeedRepository(
    private val apiGateway: ApigatewayServiceBlockingStub
) {
    suspend fun loadLots(
        filters: List<Filter>,
        sortType: SortType,
    ): WaterfallResponse {
        return withContext(Dispatchers.IO) {
            val waterfallRequest = waterfallRequest {
                this.pageSize = PAGE_SIZE
                this.filters.addAll(filters)

                when (sortType) {
                    SortType.DATE -> {
                        this.sortByDate = sortByDate { this.typeSort = TypeSort.DESC }
                    }
                    SortType.PRICE -> {
                        this.sortByPrice = sortByPrice { this.typeSort = TypeSort.DESC }
                    }
                }
            }

            val request = apiGatewayRequest {
                this.requestBody = waterfallRequest.toByteString()
                this.methodAlias = WATERFALL_METHOD_NAME
            }


            val response = apiGateway.callApiGateway(request)

            val responseBuilder = WaterfallResponse.newBuilder()
            JsonFormat.parser().merge(response.responseBody.toStringUtf8(), responseBuilder)

            responseBuilder.build()
        }
    }
}

private fun SortType.toGrpcSortType(): Any {
    return when (this) {
        SortType.DATE -> SortByDate
            .newBuilder()
            .setTypeSort(TypeSort.DESC)
            .build()
        SortType.PRICE -> TODO()
    }
}