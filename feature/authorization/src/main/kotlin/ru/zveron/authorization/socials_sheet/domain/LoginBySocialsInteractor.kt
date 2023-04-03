package ru.zveron.authorization.socials_sheet.domain

import ru.zveron.authorization.model.AuthProvider
import ru.zveron.authorization.socials_sheet.data.LoginBySocialsRepository
import ru.zveron.authorization.storage.AuthorizationStorage

class LoginBySocialsInteractor(
    private val authorizationStorage: AuthorizationStorage,
    private val loginBySocialsRepository: LoginBySocialsRepository,
) {
    suspend fun loginWithGoogle(accessToken: String) {
        val fingerprint = authorizationStorage.deviceFingerPrint ?: return
        val result = loginBySocialsRepository.loginBySocial(accessToken, AuthProvider.GOOGLE, fingerprint)

        authorizationStorage.updateAccessToken(result.accessToken.value, result.accessToken.expiresIn)
        authorizationStorage.updateRefreshToken(result.refreshToken.value, result.refreshToken.expiresIn)
    }
}