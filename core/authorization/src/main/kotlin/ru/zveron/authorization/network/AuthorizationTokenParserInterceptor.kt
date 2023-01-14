package ru.zveron.authorization.network

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import ru.zveron.authorization.base_api.TokenParser
import ru.zveron.authorization.base_api.TokenResponse
import ru.zveron.authorization.storage.AuthorizationStorage

class AuthorizationTokenParserInterceptor(
    private val authorizationStorage: AuthorizationStorage,
    private val tokenParser: TokenParser,
    moshi: Moshi,
) : Interceptor {
    private val tokenResponseJsonAdapter = moshi.adapter(TokenResponse::class.java)
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url()
        val response = chain.proceed(chain.request())

        val tokenResponse = getTokenResponse(response)

        val accessToken = tokenParser.parseAccessTokenFromResponse(response.headers())
        accessToken?.let {
            authorizationStorage.updateAccessToken(accessToken, tokenResponse?.expiresIn?.toLong())
        }

        val refreshToken = tokenParser.parseRefreshTokenFromResponse(url, response.headers())
        refreshToken?.let {
            authorizationStorage.updateRefreshToken(refreshToken, tokenResponse?.refreshExpiresIn)
        }

        return response
    }

    private fun getTokenResponse(response: Response): TokenResponse? {
        return try {
            response.body()?.string()?.let {
                tokenResponseJsonAdapter.fromJson(it)
            }
        } catch (e: JsonDataException) {
            null
        }
    }
}