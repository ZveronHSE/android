package ru.zveron.authorization.phone.password.ui

import androidx.compose.runtime.Immutable

@Immutable
data class PasswordUiState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
)