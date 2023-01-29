package ru.zveron.authorization.network.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.zveron.authorization.base_api.TokenResponse

interface RefreshTokenApi {
    @POST("/api/auth/refresh-token")
    fun refreshToken(
        @Header("Cookie") refreshCookie: String,
        @Body refreshTokenRequest: RefreshTokenRequest,
    ): Call<TokenResponse>
}