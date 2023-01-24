package ru.zveron.authorization.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.zveron.authorization.storage.AuthorizationStorage

internal class AuthInterceptor(
    private val authorizationStorage: AuthorizationStorage,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = authorizationStorage.accessToken
        val request = chain.request().newBuilder()
        token?.let {
            request.addHeader("Authorization", token)
        }
        return chain.proceed(request.build())
    }
}