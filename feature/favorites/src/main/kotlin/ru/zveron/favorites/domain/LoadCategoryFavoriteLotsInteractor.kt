package ru.zveron.favorites.domain

import ru.zveron.favorites.data.FavoritesRepository
import ru.zveron.models.lots.Lot

class LoadCategoryFavoriteLotsInteractor(
    private val favoritesRepository: FavoritesRepository,
) {
    suspend fun loadFavoriteLots(categoryId: Int): List<Lot> {
        return favoritesRepository.getFavoriteLots(categoryId)
    }
}