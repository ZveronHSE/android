package ru.zveron.create_lot.price_step

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.zveron.create_lot.price_step.ui.PriceStepViewModel

internal val priceStepModule = module {
    scope<PriceStepComponent> {
        viewModelOf(::PriceStepViewModel)
    }
}