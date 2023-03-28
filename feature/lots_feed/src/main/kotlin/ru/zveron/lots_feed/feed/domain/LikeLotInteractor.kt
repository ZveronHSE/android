package ru.zveron.lots_feed.feed.domain

import ru.zveron.favorites.data.FavoritesRepository

class LikeLotInteractor(
    private val favoritesRepository: FavoritesRepository,
) {
    suspend fun setLotFavoriteStatus(lotId: Long, newFavoriteStatus: Boolean) {
        if (newFavoriteStatus) {
            favoritesRepository.addLotToFavorites(lotId)
        } else {
            favoritesRepository.removeLotFromFavorites(lotId)
        }
    }
}