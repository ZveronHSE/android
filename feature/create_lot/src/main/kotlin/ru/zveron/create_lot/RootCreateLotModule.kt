package ru.zveron.create_lot

import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.create_lot.data.LotCreatePhotoStateRepository
import ru.zveron.create_lot.data.LotCreateSelectedCategoriesRepository
import ru.zveron.create_lot.domain.LotCreateUploadPhotoInteractor
import ru.zveron.create_lot.first_step.firstStepModule

val rootCreateLotModule = module {
    scope<RootCreateLotComponent> {
        scopedOf(::LotCreateSelectedCategoriesRepository)
        scopedOf(::LotCreatePhotoStateRepository)
        scopedOf(::LotCreateUploadPhotoInteractor)
        scoped<CreateLotScopeDelegate> {
            get<RootCreateLotComponent>()
        }
    }

    includes(
        firstStepModule,
    )
}