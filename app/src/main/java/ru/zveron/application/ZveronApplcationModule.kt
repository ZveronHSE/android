package ru.zveron.application

import com.squareup.moshi.Moshi
import org.koin.dsl.module
import ru.zveron.authorization.authorizationModule
import ru.zveron.authorization.interceptorsModule
import ru.zveron.authorization.phone.rootPhoneModule
import ru.zveron.lots_feed.feed.lotsFeedModule
import ru.zveron.lots_feed.filters_screen.filtersModule
import ru.zveron.main_screen.bottom_navigation.lots_feed_backstack.lotsFeedBackStackModule
import ru.zveron.main_screen.mainScreenModule

private val applicationSingletonModule = module {
    single<Moshi> {
        Moshi.Builder().build()
    }
}

internal val appModule = module {
    includes(
        applicationSingletonModule,
        networkModule,
        authorizationModule,
        interceptorsModule,

        rootPhoneModule,

        mainScreenModule,

        lotsFeedBackStackModule,
        lotsFeedModule,
        filtersModule,
    )
}
