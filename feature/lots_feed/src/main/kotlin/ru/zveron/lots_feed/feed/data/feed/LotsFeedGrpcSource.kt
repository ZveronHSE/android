package ru.zveron.lots_feed.feed.data.feed

import ru.zveron.categories.models.Category
import ru.zveron.contract.lot.WaterfallRequest
import ru.zveron.contract.lot.WaterfallResponse
import ru.zveron.contract.lot.model.parameter
import ru.zveron.contract.lot.waterfallRequest
import ru.zveron.lots_feed.filters_screen.data.parameters.ParameterState
import ru.zveron.lots_feed.mappings.addSortType
import ru.zveron.lots_feed.mappings.toGrpcFilter
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.models.lots.Lot
import ru.zveron.models.mappings.toDomainLot
import ru.zveron.network.ApigatewayDelegate

private const val WATERFALL_METHOD_NAME = "waterfallGet"

class LotsFeedGrpcSource(
    private val apiGateway: ApigatewayDelegate,
): LotsFeedSource {
    override suspend fun loadLots(
        filters: List<Filter>,
        sortType: SortType,
        pageSize: Int,
        parameters: List<ParameterState>?,
        category: Category?,
        query: String?
    ): List<Lot> {
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

        val response = apiGateway.callApiGateway<WaterfallRequest, WaterfallResponse>(
            WATERFALL_METHOD_NAME,
            waterfallRequest,
            WaterfallResponse.newBuilder(),
        )

        return response.lotsList.map { it.toDomainLot() }
    }
}