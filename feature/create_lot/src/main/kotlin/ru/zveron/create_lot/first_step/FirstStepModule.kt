package ru.zveron.create_lot.first_step

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.zveron.create_lot.first_step.ui.FirstStepViewModel

internal val firstStepModule = module {
    scope<FirstStepComponent> {
        viewModelOf(::FirstStepViewModel)
    }
}