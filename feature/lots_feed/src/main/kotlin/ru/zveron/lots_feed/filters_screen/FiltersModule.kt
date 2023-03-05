package ru.zveron.lots_feed.filters_screen

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.zveron.lots_feed.filters_screen.data.ParametersGrpcSource
import ru.zveron.lots_feed.filters_screen.data.ParametersRepository
import ru.zveron.lots_feed.filters_screen.data.ParametersSource
import ru.zveron.lots_feed.filters_screen.domain.ParametersHolder
import ru.zveron.lots_feed.filters_screen.ui.FiltersViewModel
import ru.zveron.lots_feed.filters_screen.ui.root_categories.RootCategoriesViewModel

val filtersModule = module {
    scope<FiltersComponent> {
        viewModelOf(::FiltersViewModel)
        viewModelOf(::RootCategoriesViewModel)

        scopedOf(::ParametersHolder)

        scopedOf(::ParametersRepository)

        scopedOf(::ParametersGrpcSource) bind ParametersSource::class
    }
}