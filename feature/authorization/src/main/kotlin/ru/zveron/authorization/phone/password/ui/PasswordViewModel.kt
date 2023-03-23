package ru.zveron.authorization.phone.password.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.authorization.phone.password.domain.PasswordLoginInteractor

private const val PHONE_LENGTH = 10

class PasswordViewModel(
    private val passwordLoginInteractor: PasswordLoginInteractor,
): ViewModel() {
    private val _stateFlow = MutableStateFlow(PasswordUiState())
    val stateFlow = _stateFlow.asStateFlow()

    val phoneState = mutableStateOf("")
    val passwordState = mutableStateOf("")

    private val _finishRegistrationFlow = MutableSharedFlow<Unit>()
    val finishRegistrationFlow = _finishRegistrationFlow.asSharedFlow()

    fun login() {
        _stateFlow.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                passwordLoginInteractor.loginWithPassword(phoneState.value, passwordState.value)
                _stateFlow.update { it.copy(isError = false, isLoading = false) }
                _finishRegistrationFlow.emit(Unit)
            } catch (e: Exception) {
                Log.e("Password", "Error login with password", e)
                _stateFlow.update { it.copy(isError = true, isLoading = false) }
            }
        }
    }

    fun canLogin(phone: String, password: String, state: PasswordUiState): Boolean {
        return phone.length == PHONE_LENGTH && password.isNotBlank() && !state.isLoading
    }
}