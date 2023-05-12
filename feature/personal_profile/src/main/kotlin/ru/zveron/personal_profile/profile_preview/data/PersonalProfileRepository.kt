package ru.zveron.personal_profile.profile_preview.data

import android.util.Log
import com.google.protobuf.Empty
import com.google.protobuf.empty
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.contract.profile.GetProfileInfoResponse
import ru.zveron.network.ApigatewayDelegate
import ru.zveron.personal_profile.mappings.toDomain

private const val GET_PROFILE_INFO_METHOD_NAME = "profileGetInfo"

internal class PersonalProfileRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    private val _profileInfoState = MutableStateFlow<ProfileInfoState>(ProfileInfoState.Loading)
    val profileInfoState = _profileInfoState.asStateFlow()

    suspend fun fetchPersonalProfileInfo() {
        try {
            _profileInfoState.update { ProfileInfoState.Loading }
            val request = empty { }

            val response = apigatewayDelegate.callApiGateway<Empty, GetProfileInfoResponse>(
                methodName = GET_PROFILE_INFO_METHOD_NAME,
                body = request,
                responseBuilder = GetProfileInfoResponse.newBuilder(),
            )

            val profileInfo = ProfileInfo(
                id = response.id,
                name = response.name,
                surname = response.surname,
                avatarUrl = response.imageUrl,
                rating = response.rating,
                addressInfo = response.address.toDomain(),
            )
            _profileInfoState.update { ProfileInfoState.Success(profileInfo) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("Personal profile", "Error fetching personal profile info", e)
            _profileInfoState.update { ProfileInfoState.Error }
        }
    }

    fun getCachedProfileInfo(): ProfileInfo {
        return (profileInfoState.value as ProfileInfoState.Success).profileInfo
    }

    fun updateWithProfileInfo(info: ProfileInfo) {
        _profileInfoState.update { ProfileInfoState.Success(info) }
    }
}

internal sealed class ProfileInfoState {
    object Loading: ProfileInfoState()

    object Error: ProfileInfoState()

    data class Success(val profileInfo: ProfileInfo): ProfileInfoState()
}