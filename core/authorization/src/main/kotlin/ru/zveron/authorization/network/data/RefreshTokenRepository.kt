package ru.zveron.authorization.network.data

import ru.zveron.authorization.network.data.api.RefreshTokenApi
import ru.zveron.authorization.network.data.api.RefreshTokenRequest
import ru.zveron.network.cookies.CookieFactory

private const val REFRESH_TOKEN_COOKIE = "refreshToken"

class RefreshTokenRepository(
    private val refreshTokenApi: RefreshTokenApi,
    private val cookieFactory: CookieFactory,
) {
    fun refreshToken(refreshToken: String, fingerPrint: String) {
        val cookie = cookieFactory.createCookieHeader(
            REFRESH_TOKEN_COOKIE to refreshToken
        )
        val refreshTokenRequest = RefreshTokenRequest(fingerPrint)

        val call = refreshTokenApi.refreshToken(cookie, refreshTokenRequest)
        // updating tokens is done as side-effect in AuthorizationTokenParserInterceptor
        call.execute()
    }
}