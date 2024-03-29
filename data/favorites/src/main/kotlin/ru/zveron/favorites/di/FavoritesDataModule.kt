package ru.zveron.favorites.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.zveron.favorites.data.FavoritesRepository
import ru.zveron.favorites.data.RemoveFavoritesRepository

val favoritesDataModule = module {
    singleOf(::FavoritesRepository)

    singleOf(::RemoveFavoritesRepository)
}