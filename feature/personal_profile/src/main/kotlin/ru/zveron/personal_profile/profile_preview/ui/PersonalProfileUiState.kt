package ru.zveron.personal_profile.profile_preview.ui

import androidx.compose.runtime.Immutable
import ru.zveron.design.resources.ZveronImage

@Immutable
sealed class PersonalProfileUiState {
    object Loading: PersonalProfileUiState()

    object Error: PersonalProfileUiState()

    data class Success(
        val avatar: ZveronImage,
        val rating: Double,
        val displayName: String,
        val isRefreshing: Boolean = false,
        val isLogoutting: Boolean = false,
    ): PersonalProfileUiState()
}