package ru.zveron.network.cookies

class CookieFactory {
    fun createCookieHeader(vararg cookiePairs: Pair<String, String>): String {
        return cookiePairs.joinToString(";") {(key, value) ->
            "$key=$value"
        }
    }
}