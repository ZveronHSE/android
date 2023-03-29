package ru.zveron.authorization.phone.registration.ui

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
import ru.zveron.authorization.phone.registration.domain.RegistrationInteractor

class RegistrationViewModel(
    private val sessionId: String,
    private val registrationInteractor: RegistrationInteractor,
): ViewModel() {
    val usernameState = mutableStateOf("")
    val surnameState = mutableStateOf("")
    val passwordState = mutableStateOf("")

    private val _finishRegistrationFlow = MutableSharedFlow<Unit>()
    val finishRegistrationFlow = _finishRegistrationFlow.asSharedFlow()

    private val _registrationStateFlow = MutableStateFlow(RegistrationUiState())
    val registrationUiState = _registrationStateFlow.asStateFlow()

    fun register() {
        _registrationStateFlow.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                registrationInteractor.register(
                    sessionId = sessionId,
                    name = usernameState.value,
                    surname = surnameState.value,
                    password = passwordState.value,
                )
                _registrationStateFlow.update { it.copy(isLoading = false) }
                _finishRegistrationFlow.emit(Unit)
            } catch (e: Exception) {
                Log.e("Registration", "Error registering with password", e)
                _registrationStateFlow.update { it.copy(isLoading = false) }
            }
        }
    }
}