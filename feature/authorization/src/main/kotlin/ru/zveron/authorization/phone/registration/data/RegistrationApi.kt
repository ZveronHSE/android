package ru.zveron.authorization.phone.registration.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationApi {
    @POST("/api/auth/register")
    suspend fun register(@Body request: RegistrationRequest): Response<Unit>
}