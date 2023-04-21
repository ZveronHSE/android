package ru.zveron.application

import org.koin.dsl.module
import ru.zveron.authorization.authorizationModule
import ru.zveron.authorization.interceptorsModule
import ru.zveron.authorization.phone.rootPhoneModule
import ru.zveron.authorization.socials_sheet.socialsModule
import ru.zveron.create_lot.root.rootCreateLotModule
import ru.zveron.favorites.di.favoritesDataModule
import ru.zveron.favorites.ui.favoritesModule
import ru.zveron.image_storage.imageStorageModule
import ru.zveron.lot_card.lotCardModule
import ru.zveron.lots_feed.feed.lotsFeedModule
import ru.zveron.lots_feed.filters_screen.filtersModule
import ru.zveron.main_screen.bottom_navigation.lots_feed_backstack.lotsFeedBackStackModule
import ru.zveron.main_screen.mainScreenModule
import ru.zveron.network.apigatewayModule
import ru.zveron.parameters.di.parametersModule
import ru.zveron.platform.platformModule
import ru.zveron.user_lots.userLotsModule
import ru.zveron.user_profile.userProfileModule

internal val appModule = module {
    includes(
        networkModule,
        authorizationModule,
        interceptorsModule,
        apigatewayModule,
        platformModule,

        favoritesDataModule,
        imageStorageModule,
        parametersModule,

        socialsModule,
        rootPhoneModule,

        mainScreenModule,

        lotsFeedBackStackModule,
        lotsFeedModule,
        filtersModule,

        favoritesModule,

        lotCardModule,

        userLotsModule,

        rootCreateLotModule,

        userProfileModule,
    )
}
