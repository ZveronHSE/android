package ru.zveron.user_lots.data

import ru.zveron.user_lots.domain.Lot

interface UserLotsDataSource {
    suspend fun loadLots(): List<Lot>
}