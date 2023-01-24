package ru.zveron.authorization.base_api

import okhttp3.Cookie
import okhttp3.Headers
import okhttp3.HttpUrl

class TokenParser {
    fun parseAccessTokenFromResponse(headers: Headers): String? {
        return headers.get("Authorization")
    }

    fun parseRefreshTokenFromResponse(httpUrl: HttpUrl, headers: Headers): String? {
        val cookie = Cookie.parseAll(httpUrl, headers)
        if (cookie.isEmpty()) {
            return null
        }
        return cookie.first().value
    }
}