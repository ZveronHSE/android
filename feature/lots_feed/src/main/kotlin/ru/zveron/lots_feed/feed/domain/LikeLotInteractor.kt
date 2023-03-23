package ru.zveron.lots_feed.feed.domain

import ru.zveron.lots_feed.feed.data.favorites.FeedFavoritesRepository

class LikeLotInteractor(
    private val feedFavoritesRepository: FeedFavoritesRepository,
) {
    suspend fun setLotFavoriteStatus(lotId: Long, newFavoriteStatus: Boolean) {
        if (newFavoriteStatus) {
            feedFavoritesRepository.addLotToFavorites(lotId)
        } else {
            feedFavoritesRepository.removeLotFromFavorites(lotId)
        }
    }
}