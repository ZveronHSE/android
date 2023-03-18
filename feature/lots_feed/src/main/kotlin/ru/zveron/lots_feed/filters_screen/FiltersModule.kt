package ru.zveron.lots_feed.filters_screen

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.zveron.lots_feed.BuildConfig
import ru.zveron.lots_feed.choose_item.ChooseItemHolder
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersChildrenCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersChildrenLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.filters_screen.domain.PassDataToFeedInteractor
import ru.zveron.lots_feed.filters_screen.domain.PassDataToFeedInteractorImpl
import ru.zveron.lots_feed.parameters.data.ParametersGrpcSource
import ru.zveron.lots_feed.parameters.data.ParametersMockSource
import ru.zveron.lots_feed.parameters.data.ParametersRepository
import ru.zveron.lots_feed.parameters.data.ParametersSource
import ru.zveron.lots_feed.filters_screen.domain.categories.ChildCategoryItemProvider
import ru.zveron.lots_feed.filters_screen.domain.categories.FiltersSetSelectedCategoryInteractor
import ru.zveron.lots_feed.filters_screen.domain.categories.FiltersUpdateCategoriesInteractor
import ru.zveron.lots_feed.filters_screen.domain.lot_forms.FiltersSetSelectedLotFormInteractor
import ru.zveron.lots_feed.filters_screen.domain.lot_forms.FiltersUpdateLotFormsInteractor
import ru.zveron.lots_feed.filters_screen.domain.lot_forms.LotFormItemProvider
import ru.zveron.lots_feed.filters_screen.domain.parameters.FiltersUpdateParametersInteractor
import ru.zveron.lots_feed.filters_screen.domain.parameters.ParameterItemProviderFactory
import ru.zveron.lots_feed.filters_screen.ui.categories.FiltersChildrenCategoriesViewModel
import ru.zveron.lots_feed.filters_screen.ui.categories.FiltersRootCategoriesViewModel
import ru.zveron.lots_feed.filters_screen.ui.lot_form.LotFormViewModel
import ru.zveron.lots_feed.filters_screen.ui.parameters.FiltersViewModel
import ru.zveron.lots_feed.lot_forms.data.LotFormGrpcSource
import ru.zveron.lots_feed.lot_forms.data.LotFormLocalSource
import ru.zveron.lots_feed.lot_forms.data.LotFormMockSource
import ru.zveron.lots_feed.lot_forms.data.LotFormRepository
import ru.zveron.lots_feed.lot_forms.data.LotFormSource
import ru.zveron.lots_feed.parameters.data.ParametersLocalCache

val filtersModule = module {
    scope<FiltersComponent> {
        viewModelOf(::FiltersViewModel)
        viewModelOf(::FiltersRootCategoriesViewModel)
        viewModelOf(::FiltersChildrenCategoriesViewModel)
        viewModelOf(::LotFormViewModel)

        scopedOf(::LotFormItemProvider)
        scopedOf(::ChildCategoryItemProvider)
    }

    // region categories
    singleOf(::FiltersSetSelectedCategoryInteractor)

    singleOf(::FiltersChildrenCategoryRepository)

    singleOf(::FiltersSelectedCategoryRepository)

    singleOf(::FiltersUpdateCategoriesInteractor)
    // endregion

    // region lot forms
    singleOf(::FiltersSelectedLotFormRepository)

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

    singleOf(::FiltersChildrenLotFormRepository)

    singleOf(::FiltersUpdateLotFormsInteractor)

    singleOf(::FiltersSetSelectedLotFormInteractor)
    // endregion

    // region parameters
    singleOf(::FiltersSelectedParametersRepository)

    singleOf(::ParametersLocalCache)

    singleOf(::FiltersUpdateParametersInteractor)

    singleOf(::ParameterItemProviderFactory)

    singleOf(::ParametersRepository)
    singleOf(::ParametersGrpcSource)
    singleOf(::ParametersMockSource)
    single<ParametersSource> {
        if (BuildConfig.useDebugMocks) {
            get<ParametersMockSource>()
        } else {
            get<ParametersGrpcSource>()
        }
    }
    // endregion

    singleOf(::ChooseItemHolder)

    singleOf(::PassDataToFeedInteractorImpl) bind PassDataToFeedInteractor::class
}