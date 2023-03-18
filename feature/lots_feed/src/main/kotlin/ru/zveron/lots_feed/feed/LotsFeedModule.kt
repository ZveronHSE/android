package ru.zveron.lots_feed.feed

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.zveron.lots_feed.BuildConfig
import ru.zveron.lots_feed.categories.data.CategoryGrpcSource
import ru.zveron.lots_feed.categories.data.CategoryLocalCacheSource
import ru.zveron.lots_feed.categories.data.CategoryMockSource
import ru.zveron.lots_feed.categories.data.CategoryRepository
import ru.zveron.lots_feed.categories.data.CategorySource
import ru.zveron.lots_feed.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.categories.domain.CategoryInteractor
import ru.zveron.lots_feed.categories.domain.PassDataToFiltersInteractor
import ru.zveron.lots_feed.categories.domain.SelectedCategoriesInteractor
import ru.zveron.lots_feed.categories.ui.CategoriesViewModel
import ru.zveron.lots_feed.feed.data.feed.LotsFeedGrpcSource
import ru.zveron.lots_feed.feed.data.feed.LotsFeedMockSource
import ru.zveron.lots_feed.feed.data.feed.LotsFeedRepository
import ru.zveron.lots_feed.feed.data.feed.LotsFeedSource
import ru.zveron.lots_feed.feed.data.sort_type.SelectedSortTypeRepository
import ru.zveron.lots_feed.feed.domain.LoadFeedInteractor
import ru.zveron.lots_feed.feed.domain.SelectSortTypeInteractor
import ru.zveron.lots_feed.feed.domain.UpdateFeedInteractor
import ru.zveron.lots_feed.feed.ui.LotsFeedViewModel

val lotsFeedModule = module {
    scope<LotsFeedComponent> {
        viewModelOf(::LotsFeedViewModel)

        viewModelOf(::CategoriesViewModel)
        scopedOf(::CategoryInteractor)

        scopedOf(::PassDataToFiltersInteractor)
    }

    singleOf(::LotsFeedRepository)
    singleOf(::LotsFeedGrpcSource)
    single<LotsFeedSource> {
        if (BuildConfig.useDebugMocks) {
            LotsFeedMockSource()
        } else {
            get<LotsFeedGrpcSource>()
        }
    }

    singleOf(::CategoryRepository)
    singleOf(::CategoryGrpcSource)
    single<CategorySource> {
        if (BuildConfig.useDebugMocks) {
            CategoryMockSource()
        } else {
            get<CategoryGrpcSource>()
        }
    }
    singleOf(::CategoryLocalCacheSource)

    singleOf(::SelectedCategoriesRepository)
    singleOf(::SelectedCategoriesInteractor)

    singleOf(::SelectSortTypeInteractor)
    singleOf(::SelectedSortTypeRepository)

    singleOf(::UpdateFeedInteractor)
    singleOf(::LoadFeedInteractor)
}