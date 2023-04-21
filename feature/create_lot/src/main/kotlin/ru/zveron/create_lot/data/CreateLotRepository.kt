package ru.zveron.create_lot.data

import ru.zveron.contract.lot.CardLot
import ru.zveron.contract.lot.CreateLotRequest
import ru.zveron.network.ApigatewayDelegate

private const val CREATE_LOT_METHOD_NAME = "lotCreate"

internal class CreateLotRepository(
    private val lotCreateInfoRepository: LotCreateInfoRepository,
    private val lotCreateSelectedCategoriesRepository: LotCreateSelectedCategoriesRepository,
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun createLot(): Long {
        val request =
            lotCreateInfoRepository.buildRequest(lotCreateSelectedCategoriesRepository.currentCategorySelection.value)

        val response = apigatewayDelegate.callApiGateway<CreateLotRequest, CardLot>(
            CREATE_LOT_METHOD_NAME,
            request,
            CardLot.newBuilder(),
        )

        return response.id
    }
}