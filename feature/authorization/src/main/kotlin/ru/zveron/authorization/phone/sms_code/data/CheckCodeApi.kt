package ru.zveron.authorization.phone.sms_code.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.zveron.authorization.phone.phone_input.data.basePhoneApi

interface CheckCodeApi {
    @POST("$basePhoneApi/check")
    suspend fun checkCode(@Body checkCodeRequest: CheckCodeRequest): Response<Unit>
}