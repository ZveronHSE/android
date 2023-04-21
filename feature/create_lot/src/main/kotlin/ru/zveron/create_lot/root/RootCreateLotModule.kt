package ru.zveron.create_lot.root

import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.create_lot.CreateLotScopeDelegate
import ru.zveron.create_lot.address_channels_step.addressChannelsStepModule
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.create_lot.data.LotCreatePhotoStateRepository
import ru.zveron.create_lot.data.LotCreateSelectedCategoriesRepository
import ru.zveron.create_lot.data.PhotoConversionCacheRepository
import ru.zveron.create_lot.categories_step.CategoriesItemProvider
import ru.zveron.create_lot.data.CreateLotRepository
import ru.zveron.create_lot.data.LotCreateAddressRepository
import ru.zveron.create_lot.details_step.detailsStepModule
import ru.zveron.create_lot.details_step.domain.ParametersItemProviderFactory
import ru.zveron.create_lot.domain.LotCreateUploadPhotoInteractor
import ru.zveron.create_lot.domain.ShouldInputGenderInteractor
import ru.zveron.create_lot.gender_step.GenderItemProvider
import ru.zveron.create_lot.general_step.generalStepModule
import ru.zveron.create_lot.lot_form_step.LotFormItemProvider
import ru.zveron.create_lot.price_step.priceStepModule

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
        scopedOf(::LotFormItemProvider)
        scopedOf(::ParametersItemProviderFactory)
        scopedOf(::LotCreateAddressRepository)
        scopedOf(::CreateLotRepository)
        scopedOf(::ShouldInputGenderInteractor)
        scopedOf(::GenderItemProvider)
    }

    includes(
        generalStepModule,
        detailsStepModule,
        priceStepModule,
        addressChannelsStepModule,
    )
}