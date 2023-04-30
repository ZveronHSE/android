package ru.zveron.authorization.network.domain

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.zveron.authorization.network.data.RefreshTokenRepository
import ru.zveron.authorization.storage.AuthorizationStorage

class RefreshTokenInteractor(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val authorizationStorage: AuthorizationStorage,
) {
    private var checkTokenJob: Job? = null
    private var updateTokenJob: Job? = null

    suspend fun checkTokenExpiration() = coroutineScope {
        val job = checkTokenJob
        if (job != null) {
            job.join()
            return@coroutineScope
        }

        checkTokenJob = launch {
            val accessTokenExpiration = authorizationStorage.accessTokenExpiration
            val currentTime = System.currentTimeMillis()

            if (accessTokenExpiration != null && currentTime >= accessTokenExpiration) {
                refreshToken()
            }
        }
    }

    suspend fun refreshToken() = coroutineScope {
        val job = updateTokenJob
        if (job != null) {
            job.join()
            return@coroutineScope
        }

        updateTokenJob = launch {
            val fingerprint = authorizationStorage.deviceFingerPrint ?: return@launch

            val refreshToken = authorizationStorage.refreshToken ?: return@launch

            refreshTokenRepository.refreshToken(refreshToken, fingerprint)
        }
    }
}