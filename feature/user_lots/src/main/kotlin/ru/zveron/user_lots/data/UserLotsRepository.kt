package ru.zveron.user_lots.data

import ru.zveron.user_lots.domain.Lot

class UserLotsRepository(
    private val userLotsDataSource: UserLotsDataSource,
) {
    suspend fun loadLots(active: Boolean): List<Lot> {
        return userLotsDataSource.loadLots(active)
    }
}