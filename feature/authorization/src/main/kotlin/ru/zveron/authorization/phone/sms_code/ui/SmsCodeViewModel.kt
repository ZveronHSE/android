package ru.zveron.authorization.phone.sms_code.ui

import android.util.Log
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
import ru.zveron.authorization.phone.sms_code.domain.CheckCodeResult

internal const val DESIRED_CODE_LENGTH = 4
private const val CODE_DELAY_IN_SECONDS = 30

class SmsCodeViewModel(
    private val smsCodeNavigator: SmsCodeNavigator,
    phoneFormatter: PhoneFormatter,
    private val phoneNumber: String,
    sessionId: String,
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

    private var currentSessionId = sessionId

    private fun smsCodeInputted() {
        _stateFlow.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val result = checkCodeInteractor.checkCode(currentSessionId, codeState.value)
                _stateFlow.update { it.copy(isLoading = false, isError = false) }
                when (result) {
                    is CheckCodeResult.NeedRegister -> smsCodeNavigator.navigateToRegistration(result.sessionId)
                    CheckCodeResult.Ready -> _finishRegistrationFlow.emit(Unit)
                }
            } catch (e: Exception) {
                Log.e("Sms", "Error verifying sms code", e)

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
            try {
                val sessionId = phoneSendRepository.sendPhone(phoneNumber, currentSessionId)
                currentSessionId = sessionId
            } catch (e: Exception) {
                Log.e("Sms", "Error requesting sms code again", e)
            }
        }
    }

    private fun getTickerFlow(): Flow<Int> {
        return (CODE_DELAY_IN_SECONDS downTo 1)
            .asFlow()
            .onEach { secondsLeft ->
                _stateFlow.update {
                    it.copy(codeRequestState = CodeRequestState.NeedToWait(secondsLeft))
                }
            }
            .onEach { delay(1000L) }
            .onCompletion {
                if (it !is CancellationException) {
                    _stateFlow.update { state ->
                        state.copy(codeRequestState = CodeRequestState.CanRequestCode)
                    }
                }
            }
    }
}