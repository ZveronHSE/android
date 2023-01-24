package ru.zveron.authorization.phone.phone_input.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.authorization.phone.phone_input.data.PhoneSendRepository
import ru.zveron.authorization.phone.phone_input.deps.PhoneInputNavigator

private const val PHONE_LENGTH = 10

class PhoneInputViewModel(
    private val phoneInputNavigator: PhoneInputNavigator,
    private val phoneSendRepository: PhoneSendRepository,
): ViewModel() {
    val textState = mutableStateOf("")

    private val _stateFlow = MutableStateFlow(PhoneInputState())
    val stateFlow = _stateFlow.asStateFlow()

    fun continueClicked() {
        val inputtedPhone = textState.value
        _stateFlow.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val result = phoneSendRepository.sendPhone(inputtedPhone)
            if (result) {
                _stateFlow.update { it.copy(isLoading = false, isError = false) }
                phoneInputNavigator.navigateToSmsScreen(inputtedPhone)
            } else {
                _stateFlow.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

    fun canContinue(phone: String, state: PhoneInputState): Boolean {
        return phone.length == PHONE_LENGTH && !state.isLoading
    }
}