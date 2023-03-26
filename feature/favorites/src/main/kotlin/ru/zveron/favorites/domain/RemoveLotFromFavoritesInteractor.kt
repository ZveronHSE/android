package ru.zveron.favorites.domain

import ru.zveron.favorites.data.FavoritesRepository

internal class RemoveLotFromFavoritesInteractor(
    private val favoritesRepository: FavoritesRepository,
) {
    suspend fun removeLotFromFavorites(lotId: Long) {
        favoritesRepository.removeLotFromFavorites(lotId)
    }
}