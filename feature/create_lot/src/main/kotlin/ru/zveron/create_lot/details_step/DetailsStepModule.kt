package ru.zveron.create_lot.details_step

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.create_lot.details_step.domain.LoadParametersInteractor
import ru.zveron.create_lot.details_step.ui.DetailsStepViewModel

internal val detailsStepModule = module {
    scope<DetailsStepComponent> {
        viewModelOf(::DetailsStepViewModel)
        scopedOf(::LoadParametersInteractor)
    }
}