package ru.zveron.create_lot.details_step.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zveron.create_lot.details_step.DetailsStepNavigator

class DetailsStepViewModel(
    private val navigator: DetailsStepNavigator,
): ViewModel() {
    private val _parametersUiState = MutableStateFlow<ParametersUiState>(ParametersUiState.Loading)
    val parametersUiState = _parametersUiState.asStateFlow()

    val continueButtonState = snapshotFlow { descriptionInputState.value }
        .map { ContinueButtonState(it.isNotBlank()) }
        .stateIn(viewModelScope, SharingStarted.Lazily, ContinueButtonState(false))

    val descriptionInputState = mutableStateOf("")

    fun setDescriptionInput(input: String) {
        descriptionInputState.value = input
    }

    fun onParameterClick(parameterId: Long) {

    }

    fun onContinueClick() {
        navigator.completeDetailsStep()
    }
}