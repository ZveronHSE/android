package ru.zveron.authorization.phone.password.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

private const val baseAuthApi = "/api/auth"

interface PasswordApi {
    @POST("$baseAuthApi/login/password")
    suspend fun passwordAuthorization(@Body request: PasswordApiRequest): Response<TokenRefreshResponse>
}