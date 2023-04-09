package ru.zveron.lot_card.data

import ru.zveron.contract.lot.CardLot
import ru.zveron.contract.lot.CardLotRequest
import ru.zveron.contract.lot.cardLotRequest
import ru.zveron.lot_card.domain.LotInfo
import ru.zveron.network.ApigatewayDelegate

private const val GET_LOT_METHOD_NAME = "cardLotGet"

class LotInfoRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun loadLotInfo(id: Long): LotInfo {
        val request = cardLotRequest { this.id = id }
        val response = apigatewayDelegate.callApiGateway<CardLotRequest, CardLot>(
            GET_LOT_METHOD_NAME,
            request,
            CardLot.newBuilder(),
        )

        return response.toLotInfo()
    }
}