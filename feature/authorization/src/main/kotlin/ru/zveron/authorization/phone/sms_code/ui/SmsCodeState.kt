package ru.zveron.authorization.phone.sms_code.ui

import androidx.compose.runtime.Immutable

@Immutable
data class SmsCodeState(
    val codeRequestState: CodeRequestState = CodeRequestState.CanRequestCode,
    val isError: Boolean = false,
    val isLoading: Boolean = false,
)

@Immutable
sealed class CodeRequestState {
    object CanRequestCode: CodeRequestState()

    data class NeedToWait(val secondsLeft: Int): CodeRequestState()
}