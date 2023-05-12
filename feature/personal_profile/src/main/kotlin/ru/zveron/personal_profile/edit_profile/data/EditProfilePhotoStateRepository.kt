package ru.zveron.personal_profile.edit_profile.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.personal_profile.ProfileUiInfo

internal class EditProfilePhotoStateRepository(
    params: ProfileUiInfo,
) {
    private val _photoState = MutableStateFlow(
        EditProfileAvatarState(
            PhotoUploadStatus.SUCCESS,
            uri = params.avatarUrl,
            remoteUri = params.avatarUrl,
        )
    )
    val photoState = _photoState.asStateFlow()

    fun updateState(
        status: PhotoUploadStatus,
        localUri: String?,
        remoteUri: String? = null,
    ) {
        _photoState.update { EditProfileAvatarState(status, localUri, remoteUri) }
    }
}

internal enum class PhotoUploadStatus {
    SUCCESS,
    LOADING,
    ERROR,
}

internal class EditProfileAvatarState(
    val status: PhotoUploadStatus,
    val uri: String? = null,
    val remoteUri: String? = null,
)
