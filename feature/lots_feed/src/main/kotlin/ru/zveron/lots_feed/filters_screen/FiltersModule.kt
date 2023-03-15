package ru.zveron.lots_feed.filters_screen

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.zveron.lots_feed.BuildConfig
import ru.zveron.lots_feed.choose_item.ChooseItemHolder
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersChildrenCategoryHolder
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryHolder
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersChildrenLotFormHolder
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormHolder
import ru.zveron.lots_feed.filters_screen.data.parameters.ParametersGrpcSource
import ru.zveron.lots_feed.filters_screen.data.parameters.ParametersRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.ParametersSource
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersHolder
import ru.zveron.lots_feed.filters_screen.domain.ChildCategoryItemProvider
import ru.zveron.lots_feed.filters_screen.domain.FiltersSetSelectedCategoryInteractor
import ru.zveron.lots_feed.filters_screen.domain.LotFormItemProvider
import ru.zveron.lots_feed.filters_screen.ui.FiltersViewModel
import ru.zveron.lots_feed.filters_screen.ui.categories.FiltersChildrenCategoriesViewModel
import ru.zveron.lots_feed.filters_screen.ui.categories.FiltersRootCategoriesViewModel
import ru.zveron.lots_feed.filters_screen.ui.lot_form.LotFormViewModel
import ru.zveron.lots_feed.lot_forms.data.LotFormGrpcSource
import ru.zveron.lots_feed.lot_forms.data.LotFormLocalSource
import ru.zveron.lots_feed.lot_forms.data.LotFormMockSource
import ru.zveron.lots_feed.lot_forms.data.LotFormRepository
import ru.zveron.lots_feed.lot_forms.data.LotFormSource

val filtersModule = module {
    scope<FiltersComponent> {
        viewModelOf(::FiltersViewModel)
        viewModelOf(::FiltersRootCategoriesViewModel)
        viewModelOf(::FiltersChildrenCategoriesViewModel)
        viewModelOf(::LotFormViewModel)

        scopedOf(::ParametersRepository)
        scopedOf(::ParametersGrpcSource) bind ParametersSource::class

        scopedOf(::LotFormItemProvider)
        scopedOf(::ChildCategoryItemProvider)
    }

    singleOf(::FiltersSelectedParametersHolder)

    // region lot forms
    singleOf(::FiltersSelectedLotFormHolder)

    singleOf(::LotFormRepository)
    singleOf(::LotFormLocalSource)
    singleOf(::LotFormMockSource)
    singleOf(::LotFormGrpcSource)

    single<LotFormSource> {
        if (BuildConfig.useDebugMocks) {
            get<LotFormMockSource>()
        } else {
            get<LotFormGrpcSource>()
        }
    }

    singleOf(::FiltersChildrenLotFormHolder)
    // endregion

    // region categories

    singleOf(::FiltersSetSelectedCategoryInteractor)

    singleOf(::FiltersChildrenCategoryHolder)

    singleOf(::FiltersSelectedCategoryHolder)
    // endregion

    singleOf(::ChooseItemHolder)
}