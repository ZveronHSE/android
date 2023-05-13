package ru.zveron.user_lots.data

import ru.zveron.contract.core.Status
import ru.zveron.contract.lot.GetOwnLotsRequest
import ru.zveron.contract.lot.WaterfallResponse
import ru.zveron.contract.lot.getOwnLotsRequest
import ru.zveron.network.ApigatewayDelegate
import ru.zveron.user_lots.domain.Lot
import ru.zveron.contract.core.Lot as GrpcLot

private const val USER_LOTS_METHOD_ALIAS = "lotGetOwns"

internal class GrpcUserLotsDataSource(
    private val apigatewayDelegate: ApigatewayDelegate,
): UserLotsDataSource {
    override suspend fun loadLots(
        active: Boolean,
    ): List<Lot> {
        val request = getOwnLotsRequest {
            this.onlyActive = active
            // TODO: add last lot id
        }

        val response = apigatewayDelegate.callApiGateway<GetOwnLotsRequest, WaterfallResponse>(
            USER_LOTS_METHOD_ALIAS,
            request,
            WaterfallResponse.newBuilder(),
        )

        return response.lotsList.map { it.toUserLot() }
    }

    private fun GrpcLot.toUserLot(): Lot {
        return Lot(
            id = this.id,
            title = this.title,
            price = this.price,
            imageUrl = this.imageUrl,
            isActive = this.status == Status.ACTIVE,
        )
    }
}