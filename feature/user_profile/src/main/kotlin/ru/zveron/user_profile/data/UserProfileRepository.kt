package ru.zveron.user_profile.data

import ru.zveron.contract.profile.GetProfilePageRequest
import ru.zveron.contract.profile.GetProfilePageResponse
import ru.zveron.contract.profile.getProfilePageRequest
import ru.zveron.network.ApigatewayDelegate

private const val GET_PROFILE_PAGE = "profileGetPage"

internal class UserProfileRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun getUserProfilePage(id: Long): UserProfile {
        val request = getProfilePageRequest {
            this.requestedProfileId = id
        }

        val response = apigatewayDelegate.callApiGateway<GetProfilePageRequest, GetProfilePageResponse>(
            GET_PROFILE_PAGE,
            request,
            GetProfilePageResponse.newBuilder(),
        )

        return response.toUserProfile()
    }
}