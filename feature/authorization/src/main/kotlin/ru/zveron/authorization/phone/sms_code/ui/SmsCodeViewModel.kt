package ru.zveron.authorization.phone.sms_code.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.authorization.phone.formatting.PhoneFormatter
import ru.zveron.authorization.phone.phone_input.data.PhoneSendRepository
import ru.zveron.authorization.phone.sms_code.deps.SmsCodeNavigator
import ru.zveron.authorization.phone.sms_code.domain.CheckCodeInteractor

internal const val DESIRED_CODE_LENGTH = 4
private const val CODE_DELAY_IN_SECONDS = 30

class SmsCodeViewModel(
    private val smsCodeNavigator: SmsCodeNavigator,
    phoneFormatter: PhoneFormatter,
    private val phoneNumber: String,
    private val phoneSendRepository: PhoneSendRepository,
    private val checkCodeInteractor: CheckCodeInteractor,
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(SmsCodeState())
    val stateFlow = _stateFlow.asStateFlow()

    private val _finishRegistrationFlow = MutableSharedFlow<Unit>()
    val finishRegistrationFlow = _finishRegistrationFlow.asSharedFlow()

    private var tickerJob: Job? = null

    val phoneNumberState = mutableStateOf(
        phoneFormatter.formatPhoneToVisual(phoneNumber)
    )

    val codeState = mutableStateOf("")

    private fun smsCodeInputted() {
        _stateFlow.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = checkCodeInteractor.checkCode(codeState.value, phoneNumber)
            if (result != null) {
                _stateFlow.update { it.copy(isLoading = false, isError = false) }
                if (result.isNewUser) {
                    smsCodeNavigator.navigateToRegistration(phoneNumber)
                } else {
                    // TODO: think of how to finish registration
                }
            } else {
                codeState.value = ""
                _stateFlow.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

    fun onCodeChanged(newCode: String) {
        codeState.value = newCode
        if (newCode.length == DESIRED_CODE_LENGTH) {
            smsCodeInputted()
        }
    }

    fun passwordClicked() {
        smsCodeNavigator.navigateToPassword()
    }

    fun launchTicker() {
        tickerJob?.cancel()
        tickerJob = getTickerFlow().launchIn(viewModelScope)
    }

    fun requestCodeClicked() {
        launchTicker()
        viewModelScope.launch {
            phoneSendRepository.sendPhone(phoneNumber)
        }
    }

    private fun getTickerFlow(): Flow<Int> {
        return (CODE_DELAY_IN_SECONDS downTo 1)
            .asFlow()
            .onEach { delay(1000L) }
            .onEach { secondsLeft ->
                _stateFlow.update {
                    it.copy(codeRequestState = CodeRequestState.NeedToWait(secondsLeft))
                }
            }
            .onCompletion {
                if (it !is CancellationException) {
                    _stateFlow.update { state ->
                        state.copy(codeRequestState = CodeRequestState.CanRequestCode)
                    }
                }
            }
    }
}