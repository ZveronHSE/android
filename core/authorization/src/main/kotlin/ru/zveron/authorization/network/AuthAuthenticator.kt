package ru.zveron.authorization.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.zveron.authorization.network.domain.RefreshTokenInteractor
import ru.zveron.authorization.storage.AuthorizationStorage

class AuthAuthenticator(
    private val authorizationStorage: AuthorizationStorage,
    private val refreshTokenInteractor: RefreshTokenInteractor,
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return route?.let {
            refreshTokenInteractor.refreshToken(route.address().url())
            authorizationStorage.accessToken?.let { token ->
                response.request().newBuilder()
                    .header("Authorization", token)
                    .build()
            }
        }
    }
}