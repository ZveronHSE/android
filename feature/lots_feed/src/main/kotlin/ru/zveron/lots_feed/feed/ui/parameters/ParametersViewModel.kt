package ru.zveron.lots_feed.feed.ui.parameters

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.lots_feed.categories.domain.SelectedCategoriesInteractor
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.feed.domain.UpdateParametersInteractorImpl
import ru.zveron.lots_feed.filters_screen.data.parameters.ParameterState

internal class ParametersViewModel(
    selectedCategoriesInteractor: SelectedCategoriesInteractor,
    selectedParametersRepository: SelectedParametersRepository,
    private val updateParametersInteractor: UpdateParametersInteractorImpl,
): ViewModel() {
    private val _isLoadingState = MutableStateFlow(true)

    val uiState = combine(
        _isLoadingState,
        selectedCategoriesInteractor.currentCategorySelection,
        selectedParametersRepository.parametersState,
    ) { isLoading, categorySelection, parameterSelection ->
        when {
            categorySelection.innerCategory == null -> ParametersUiState.Hidden
            isLoading -> ParametersUiState.Loading
            parameterSelection.isEmpty() -> ParametersUiState.Hidden
            else -> ParametersUiState.Success(mapParameterState(parameterSelection))
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ParametersUiState.Hidden)

    init {
        updateParametersInteractor.updateFlow
            .onEach { loadParameters() }
            .launchIn(viewModelScope)
    }

    private fun loadParameters() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoadingState.update { true }
            try {
                updateParametersInteractor.loadParameters()
                _isLoadingState.update { false }
            } catch (e: Exception) {
                Log.e("Parameters", "Error loading parameters", e)
            }
        }
    }

    private fun mapParameterState(parameters: List<ParameterState>): List<ParameterUiState> {
        return parameters.map {
            val title = it.value ?: it.parameter.name
            ParameterUiState(it.parameter.id, title, it.value != null)
        }
    }
}