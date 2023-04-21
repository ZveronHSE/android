package ru.zveron.create_lot.details_step.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.create_lot.details_step.DetailsStepNavigator
import ru.zveron.create_lot.details_step.domain.LoadParametersInteractor
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.models.parameters.Parameter
import ru.zveron.parameters.data.ParametersRepository

internal class DetailsStepViewModel(
    private val navigator: DetailsStepNavigator,
    private val loadParametersInteractor: LoadParametersInteractor,
    private val lotCreateInfoRepository: LotCreateInfoRepository,
    private val parametersRepository: ParametersRepository,
) : ViewModel() {
    private val _parametersUiState = MutableStateFlow<ParametersState>(ParametersState.Loading)

    val parametersUiState = combine(
        _parametersUiState,
        lotCreateInfoRepository.selectedParametersState,
    ) { state, parametersValues ->
        when (state) {
            ParametersState.Loading -> ParametersUiState.Loading
            is ParametersState.Success -> {
                val parametersUiStates = state.parameters.map {
                    val parameterValue = parametersValues[it.id]
                    ParameterUiState(
                        id = it.id,
                        title = parameterValue ?: it.name,
                        selected = parameterValue != null,
                    )
                }
                ParametersUiState.Success(ListWrapper(parametersUiStates))
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ParametersUiState.Loading)

    val continueButtonState = snapshotFlow { descriptionInputState.value }
        .map { ContinueButtonState(it.isNotBlank()) }
        .stateIn(viewModelScope, SharingStarted.Lazily, ContinueButtonState(false))

    val descriptionInputState = mutableStateOf("")

    init {
        launchLoadParameters()
    }

    fun setDescriptionInput(input: String) {
        descriptionInputState.value = input
    }

    fun onParameterClick(parameterId: Int) {
        val parameter = parametersRepository.getParameterById(parameterId)
        navigator.pickParameterValue(parameterId, parameter.name)
    }

    fun onContinueClick() {
        lotCreateInfoRepository.setDescription(descriptionInputState.value)
        navigator.completeDetailsStep()
    }

    private fun launchLoadParameters() {
        viewModelScope.launch {
            try {
                _parametersUiState.update { ParametersState.Loading }
                val parameters = loadParametersInteractor.loadParameters()
                _parametersUiState.update { ParametersState.Success(parameters) }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("Lot create", "Error loading paramters", e)
            }
        }
    }

    private sealed class ParametersState {
        object Loading: ParametersState()

        data class Success(val parameters: List<Parameter>): ParametersState()
    }
}