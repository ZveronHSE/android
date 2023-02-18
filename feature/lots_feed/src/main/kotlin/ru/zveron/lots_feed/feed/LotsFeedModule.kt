package ru.zveron.lots_feed.feed

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.lots_feed.feed.data.LotsFeedRepository
import ru.zveron.lots_feed.feed.ui.LotsFeedViewModel

val lotsFeedModule = module {
    scope<LotsFeedComponent> {
        viewModelOf(::LotsFeedViewModel)
        scopedOf(::LotsFeedRepository)
    }
}