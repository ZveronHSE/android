package ru.zveron.personal_profile.edit_profile.data

import com.google.protobuf.Empty
import ru.zveron.contract.profile.SetProfileInfoRequest
import ru.zveron.contract.profile.setProfileInfoRequest
import ru.zveron.network.ApigatewayDelegate
import ru.zveron.personal_profile.mappings.toGrpc
import ru.zveron.personal_profile.profile_preview.data.ProfileAddress

private const val EDIT_PROFILE_METHOD_NAME = "profileSetInfo"

internal class EditProfileRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun changeProfileInfo(
        name: String,
        surname: String,
        address: ProfileAddress,
        avatarUri: String?,
    ) {
        val request = setProfileInfoRequest {
            this.name = name
            this.surname = surname
            avatarUri?.let {
                this.imageUrl = it
            }
            this.address = address.toGrpc()
        }

        apigatewayDelegate.callApiGateway<SetProfileInfoRequest, Empty>(
            EDIT_PROFILE_METHOD_NAME,
            request,
            Empty.newBuilder(),
        )
    }
}