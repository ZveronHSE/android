package ru.zveron.authorization.phone.phone_input.ui

import androidx.compose.runtime.Immutable

@Immutable
data class PhoneInputState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)