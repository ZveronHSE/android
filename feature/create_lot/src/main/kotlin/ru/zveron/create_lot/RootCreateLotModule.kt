package ru.zveron.create_lot

import org.koin.dsl.module
import ru.zveron.create_lot.first_step.firstStepModule

val rootCreateLotModule = module {
    scope<RootCreateLotComponent> {

    }

    includes(
        firstStepModule,
    )
}