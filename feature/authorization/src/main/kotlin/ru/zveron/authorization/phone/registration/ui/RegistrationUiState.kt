package ru.zveron.authorization.phone.registration.ui

import androidx.compose.runtime.Immutable
import ru.zveron.design.resources.ZveronText

@Immutable
data class RegistrationUiState(
    val isLoading: Boolean = false,
    val registerButtonEnabled: Boolean = true,
    val nameErrorText: ZveronText? = null,
    val surnameErrorText: ZveronText? = null,
)