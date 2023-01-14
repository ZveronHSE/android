package ru.zveron.authorization.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.zveron.authorization.base_api.TokenParser
import ru.zveron.authorization.storage.AuthorizationStorage

class AuthorizationTokenParserInterceptor(
    private val authorizationStorage: AuthorizationStorage,
    private val tokenParser: TokenParser,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url()
        val response = chain.proceed(chain.request())

        // TODO: add token expirations

        val accessToken = tokenParser.parseAccessTokenFromResponse(response.headers())
        accessToken?.let {
            authorizationStorage.updateAccessToken(accessToken, null)
        }

        val refreshToken = tokenParser.parseRefreshTokenFromResponse(url, response.headers())
        refreshToken?.let {
            authorizationStorage.updateRefreshToken(refreshToken, null)
        }

        return response
    }
}