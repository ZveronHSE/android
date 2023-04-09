package ru.zveron.lot_card.domain

import ru.zveron.lot_card.data.LotInfoRepository

class LoadLotInfoInteractor(
    private val lotInfoRepository: LotInfoRepository,
) {
    suspend fun loadLot(id: Long): LotInfo {
        return lotInfoRepository.loadLotInfo(id)
    }
}