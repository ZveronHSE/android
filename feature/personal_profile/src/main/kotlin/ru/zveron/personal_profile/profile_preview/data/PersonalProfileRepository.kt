package ru.zveron.personal_profile.profile_preview.data

import com.google.protobuf.Empty
import com.google.protobuf.empty
import ru.zveron.contract.profile.GetProfileInfoResponse
import ru.zveron.network.ApigatewayDelegate
import ru.zveron.personal_profile.mappings.toDomain

private const val GET_PROFILE_INFO_METHOD_NAME = "profileGetInfo"

internal class PersonalProfileRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    private var currentProfile: ProfileInfo? = null

    suspend fun getPersonalProfileInfo(): ProfileInfo {
        val request = empty {  }

        val response = apigatewayDelegate.callApiGateway<Empty, GetProfileInfoResponse>(
            methodName = GET_PROFILE_INFO_METHOD_NAME,
            body = request,
            responseBuilder = GetProfileInfoResponse.newBuilder(),
        )

        val profileInfo =  ProfileInfo(
            id = response.id,
            name = response.name,
            surname = response.surname,
            avatarUrl = response.imageUrl,
            rating = response.rating,
            addressInfo = response.address.toDomain(),
        )
        currentProfile = profileInfo
        return profileInfo
    }

    fun getCachedProfileInfo(): ProfileInfo {
        return currentProfile!!
    }
}