package ru.zveron.create_lot.general_step

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.zveron.create_lot.general_step.ui.GeneralStepViewModel

internal val generalStepModule = module {
    scope<GeneralStepComponent> {
        viewModelOf(::GeneralStepViewModel)
    }
}