package ru.zveron.authorization.network.data

import okhttp3.HttpUrl
import ru.zveron.authorization.base_api.TokenParser
import ru.zveron.authorization.network.data.api.RefreshTokenApi
import ru.zveron.authorization.network.data.api.RefreshTokenRequest
import ru.zveron.network.cookies.CookieFactory

private const val REFRESH_TOKEN_COOKIE = "refreshToken"

class RefreshTokenRepository(
    private val refreshTokenApi: RefreshTokenApi,
    private val cookieFactory: CookieFactory,
    private val tokenParser: TokenParser,
) {
    fun refreshToken(httpUrl: HttpUrl, refreshToken: String, fingerPrint: String): RefreshTokenResult {
        val cookie = cookieFactory.createCookieHeader(
            REFRESH_TOKEN_COOKIE to refreshToken
        )
        val refreshTokenRequest = RefreshTokenRequest(fingerPrint)

        val call = refreshTokenApi.refreshToken(cookie, refreshTokenRequest)
        val response = call.execute()

        val newAccessToken = tokenParser.parseAccessTokenFromResponse(response.headers())
        val newRefreshToken = tokenParser.parseRefreshTokenFromResponse(httpUrl, response.headers())

        return RefreshTokenResult(
            newAccessToken!!,
            newRefreshToken!!,
            response.body()!!.expiresIn,
            response.body()!!.refreshExpiresIn,
        )
    }
}