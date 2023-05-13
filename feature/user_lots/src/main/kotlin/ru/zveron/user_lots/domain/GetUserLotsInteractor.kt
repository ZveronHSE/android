package ru.zveron.user_lots.domain

import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.user_lots.data.UserLotsRepository

class GetUserLotsInteractor(
    private val authorizationStorage: AuthorizationStorage,
    private val userLotsRepository: UserLotsRepository,
) {
    suspend fun getActiveLots(): List<Lot> {
        require(authorizationStorage.isAuthorized()) {
            IllegalStateException("user must be authorized")
        }

        return userLotsRepository.loadLots(true)
    }


    suspend fun getArchiveLots(): List<Lot> {
        require(authorizationStorage.isAuthorized()) {
            IllegalStateException("user must be authorized")
        }

        return userLotsRepository.loadLots(false)
    }
}