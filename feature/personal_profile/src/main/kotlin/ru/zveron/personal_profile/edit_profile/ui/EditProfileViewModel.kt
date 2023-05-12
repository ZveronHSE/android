package ru.zveron.personal_profile.edit_profile.ui

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.design.components.PhotoUploadState
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.personal_profile.ProfileUiInfo
import ru.zveron.personal_profile.R
import ru.zveron.personal_profile.edit_profile.data.EditProfilePhotoStateRepository
import ru.zveron.personal_profile.edit_profile.data.PhotoUploadStatus
import ru.zveron.personal_profile.edit_profile.domain.EditProfileInteractor
import ru.zveron.personal_profile.edit_profile.domain.EditProfileUploadAvatarInteractor
import ru.zveron.personal_profile.mappings.toDomain
import ru.zveron.platform.dialog.DialogManager
import ru.zveron.platform.dialog.DialogParams
import ru.zveron.design.R as DesignR

internal class EditProfileViewModel(
    private val params: ProfileUiInfo,
    editProfilePhotoStateRepository: EditProfilePhotoStateRepository,
    private val editProfileUploadAvatarInteractor: EditProfileUploadAvatarInteractor,
    private val editProfileInteractor: EditProfileInteractor,
    private val dialogManager: DialogManager,
) : ViewModel() {
    private val _finishEditResultFlow = MutableSharedFlow<ProfileUiInfo>()
    val finishEditResultFlow = _finishEditResultFlow.asSharedFlow()


    val nameInputState = mutableStateOf(params.name)
    val surnameInputState = mutableStateOf(params.surname)

    private val _submitting = MutableStateFlow(false)

    val editPorfileUiState = combine(
        snapshotFlow { nameInputState.value },
        snapshotFlow { surnameInputState.value },
        editProfilePhotoStateRepository.photoState,
        _submitting
    ) { name, surname, photoState, submitting ->
        val canSubmit =
            name.isNotBlank() && surname.isNotBlank() && photoState.status == PhotoUploadStatus.SUCCESS && !submitting

        val image = getZveronImage(photoState.uri)
        val photoUiState = when (photoState.status) {
            PhotoUploadStatus.SUCCESS -> PhotoUploadState.Success(image)
            PhotoUploadStatus.LOADING -> PhotoUploadState.Loading(image)
            PhotoUploadStatus.ERROR -> PhotoUploadState.Error(image)
        }

        EditProfileUiState(
            photoUiState = photoUiState,
            isLoading = submitting,
            canSubmit = canSubmit,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        EditProfileUiState(
            photoUiState = PhotoUploadState.Success(getZveronImage(params.avatarUrl)),
            isLoading = false,
            canSubmit = true
        ),
    )


    private fun getZveronImage(imageUrl: String?): ZveronImage {
        return imageUrl?.let {
            ZveronImage.RemoteImage(it)
        } ?: ZveronImage.ResourceImage(DesignR.drawable.ic_no_avatar)
    }

    fun setName(value: String) {
        nameInputState.value = value
    }

    fun setSurname(value: String) {
        surnameInputState.value = value
    }

    fun uploadAvatar(uri: Uri) {
        viewModelScope.launch {
            editProfileUploadAvatarInteractor.uploadPhoto(uri.toString())
        }
    }

    fun submitClicked() {
        viewModelScope.launch {
            try {
                _submitting.update { true }
                val profileUiInfo = editProfileInteractor.editProfile(
                    nameInputState.value,
                    surnameInputState.value,
                    params.addressUiInfo.toDomain(),
                )
                _finishEditResultFlow.emit(profileUiInfo)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _submitting.update { false }
                Log.e("Personal profile edit", "Error setting profile info", e)
                dialogManager.requestDialog(
                    DialogParams(
                        title = ZveronText.RawResource(R.string.edit_profile_error_title),
                        confirmButtonLabel = ZveronText.RawResource(R.string.edit_profile_error_ok_title),
                    )
                )
            }
        }
    }

    fun photoRetryClicked() {
        viewModelScope.launch {
            editProfileUploadAvatarInteractor.retryAvatarUpload()
        }
    }
}
