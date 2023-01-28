package ru.zveron.authorization.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.zveron.authorization.storage.AuthorizationStorage

internal class AuthInterceptor(
    private val authorizationStorage: AuthorizationStorage,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath.contains("/api/phone")
            || request.url.encodedPath.contains("/api/auth")
        ) {
            return chain.proceed(request)
        }
        val token = authorizationStorage.accessToken
        val newRequest = chain.request().newBuilder()
        token?.let {
            newRequest.addHeader("Authorization", token)
        }
        return chain.proceed(newRequest.build())
    }
}