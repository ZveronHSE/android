package ru.zveron.personal_profile.edit_profile.domain

import ru.zveron.personal_profile.ProfileUiInfo
import ru.zveron.personal_profile.edit_profile.data.EditProfilePhotoStateRepository
import ru.zveron.personal_profile.edit_profile.data.EditProfileRepository
import ru.zveron.personal_profile.edit_profile.data.PhotoUploadStatus
import ru.zveron.personal_profile.mappings.toUiModel
import ru.zveron.personal_profile.profile_preview.data.ProfileAddress

internal class EditProfileInteractor(
    private val editProfileRepository: EditProfileRepository,
    private val editProfilePhotoStateRepository: EditProfilePhotoStateRepository,
) {
    suspend fun editProfile(
        name: String,
        surname: String,
        address: ProfileAddress,
    ): ProfileUiInfo {
        val photoState = editProfilePhotoStateRepository.photoState.value
        require(photoState.status == PhotoUploadStatus.SUCCESS) { IllegalStateException("no avatar uploaded") }

        val photoUrl = photoState.remoteUri

        editProfileRepository.changeProfileInfo(
            name,
            surname,
            address,
            photoUrl,
        )

        return ProfileUiInfo(
            photoUrl,
            name,
            surname,
            address.toUiModel(),
        )
    }
}