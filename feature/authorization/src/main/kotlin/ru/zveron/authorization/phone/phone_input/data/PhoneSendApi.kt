package ru.zveron.authorization.phone.phone_input.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal const val basePhoneApi = "/api/phone"

interface PhoneSendApi {
    @POST("$basePhoneApi/send")
    suspend fun sendPhone(@Body phoneSendRequest: PhoneSendRequest): Response<Unit>
}