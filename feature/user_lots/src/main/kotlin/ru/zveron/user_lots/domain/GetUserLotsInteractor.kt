package ru.zveron.user_lots.domain

import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.user_lots.data.UserLotsRepository

class GetUserLotsInteractor(
    private val authorizationStorage: AuthorizationStorage,
    private val userLotsRepository: UserLotsRepository,
) {
    suspend fun getUserLots(): UserLots {
        require(authorizationStorage.isAuthorized()) {
            IllegalStateException("user must be authorized")
        }

        val lots = userLotsRepository.loadLots()

        val activeLots = lots.filter { it.isActive }
        val archiveLots = lots.filter { !it.isActive }

        return UserLots(activeLots, archiveLots)
    }
}