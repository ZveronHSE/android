package ru.zveron.favorites.ui

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.favorites.FavoritesComponent
import ru.zveron.favorites.domain.LoadCategoryFavoriteLotsInteractor
import ru.zveron.favorites.domain.RemoveLotFromFavoritesInteractor

val favoritesModule = module {
    scope<FavoritesComponent> {
        viewModelOf(::FavoritesViewModel)

        scopedOf(::LoadCategoryFavoriteLotsInteractor)
        scopedOf(::RemoveLotFromFavoritesInteractor)
    }
}