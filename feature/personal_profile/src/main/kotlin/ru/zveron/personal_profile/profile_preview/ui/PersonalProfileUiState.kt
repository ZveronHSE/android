package ru.zveron.personal_profile.profile_preview.ui

import androidx.compose.runtime.Immutable
import ru.zveron.design.resources.ZveronImage

@Immutable
sealed class PersonalProfileUiState {
    object Loading: PersonalProfileUiState()

    data class Success(
        val avatar: ZveronImage,
        val rating: Double,
        val displayName: String,
    ): PersonalProfileUiState()
}