package ru.zveron.main_screen.bottom_navigation.lots_feed_backstack

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val lotsFeedBackStackModule = module {
    singleOf(::LotsFeedBackPlugin)
}