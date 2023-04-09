package ru.zveron.lot_card

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.lot_card.data.LotInfoRepository
import ru.zveron.lot_card.domain.LoadLotInfoInteractor
import ru.zveron.lot_card.ui.LotCardViewModel

val lotCardModule = module {
    scope<LotCardComponent> {
        viewModelOf(::LotCardViewModel)
        scopedOf(::LotInfoRepository)
        scopedOf(::LoadLotInfoInteractor)
    }
}