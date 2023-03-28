package ru.zveron.favorites.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.zveron.favorites.data.FavoritesRepository
import ru.zveron.favorites.data.RemoveFavoritesRepository
import ru.zveron.models.lots.Status

internal class RemoveLotFromFavoritesInteractor(
    private val favoritesRepository: FavoritesRepository,
    private val removeFavoritesRepository: RemoveFavoritesRepository,
) {
    suspend fun removeLotFromFavorites(lotId: Long) {
        favoritesRepository.removeLotFromFavorites(lotId)
    }

    suspend fun removeAllFromCategory(categoryId: Int) {
        removeFavoritesRepository.removeAllByCategory(categoryId)
    }

    suspend fun removeUnactiveFromCategory(categoryId: Int) = coroutineScope {
        launch(Dispatchers.IO) {
            removeFavoritesRepository.removeAllByCategoriesAndStatus(categoryId, Status.CANCELED)
        }

        launch(Dispatchers.IO) {
            removeFavoritesRepository.removeAllByCategoriesAndStatus(categoryId, Status.CLOSED)
        }
    }
}