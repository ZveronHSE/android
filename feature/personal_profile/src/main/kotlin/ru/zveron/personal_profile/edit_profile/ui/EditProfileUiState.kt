package ru.zveron.personal_profile.edit_profile.ui

import ru.zveron.design.components.PhotoUploadState

data class EditProfileUiState(
    val photoUiState: PhotoUploadState,
    val isLoading: Boolean = false,
    val canSubmit: Boolean = false,
)