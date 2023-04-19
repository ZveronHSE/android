package ru.zveron.create_lot.root

import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.create_lot.CreateLotScopeDelegate
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.create_lot.data.LotCreatePhotoStateRepository
import ru.zveron.create_lot.data.LotCreateSelectedCategoriesRepository
import ru.zveron.create_lot.data.PhotoConversionCacheRepository
import ru.zveron.categories.CategoriesItemProvider
import ru.zveron.create_lot.domain.LotCreateUploadPhotoInteractor
import ru.zveron.create_lot.general_step.generalStepModule

val rootCreateLotModule = module {
    scope<RootCreateLotComponent> {
        scopedOf(::LotCreateSelectedCategoriesRepository)
        scopedOf(::LotCreatePhotoStateRepository)
        scopedOf(::LotCreateUploadPhotoInteractor)
        scoped<CreateLotScopeDelegate> {
            get<RootCreateLotComponent>()
        }
        scopedOf(::PhotoConversionCacheRepository)
        scopedOf(::LotCreateInfoRepository)
        scopedOf(::CategoriesItemProvider)
    }

    includes(
        generalStepModule,
    )
}