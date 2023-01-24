package ru.zveron.authorization.phone.password.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.authorization.phone.password.data.PasswordRepository
import ru.zveron.authorization.phone.password.deps.PasswordNavigator

private const val PHONE_LENGTH = 10

class PasswordViewModel(
    private val passwordNavigator: PasswordNavigator,
    private val passwordRepository: PasswordRepository,
): ViewModel() {
    private val _stateFlow = MutableStateFlow(PasswordUiState())
    val stateFlow = _stateFlow.asStateFlow()

    val phoneState = mutableStateOf("")
    val passwordState = mutableStateOf("")

    fun login() {
        _stateFlow.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = passwordRepository.loginWithPassword(phoneState.value, passwordState.value)
            if (!result) {
                _stateFlow.update { it.copy(isError = true, isLoading = false) }
            } else {
                _stateFlow.update { it.copy(isError = false, isLoading = false) }
            }
        }
    }

    fun canLogin(phone: String, password: String, state: PasswordUiState): Boolean {
        return phone.length == PHONE_LENGTH && password.isNotBlank() && !state.isLoading
    }
}