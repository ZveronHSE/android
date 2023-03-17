package ru.zveron.lots_feed.filters_screen.ui.parameters

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
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.domain.parameters.FiltersUpdateParametersInteractor

internal class FiltersViewModel(
    private val filtersUpdateParametersInteractor: FiltersUpdateParametersInteractor,
    filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
) : ViewModel() {
    private val _parametersUiState =
        MutableStateFlow<FiltersParametersUiState>(FiltersParametersUiState.Loading)
    val parametersUiState = combine(
        _parametersUiState,
        filtersSelectedCategoryRepository.currentCategorySelection
    ) { uiState, categorySelection ->
        if (categorySelection.innerCategory == null) {
            FiltersParametersUiState.Hidden
        } else {
            uiState
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, FiltersParametersUiState.Hidden)

    init {
        filtersUpdateParametersInteractor.updateFlow
            .onEach { loadParameters() }
            .launchIn(viewModelScope)
    }

    private fun loadParameters() {
        viewModelScope.launch(Dispatchers.IO) {
            _parametersUiState.update { FiltersParametersUiState.Loading }
            try {
                val parameters = filtersUpdateParametersInteractor.loadParameters()

                _parametersUiState.update {
                    if (parameters.isEmpty()) {
                        FiltersParametersUiState.Hidden
                    } else {
                        FiltersParametersUiState.Success(parameters.map {
                            ParameterUiState(it.id, it.name, false)
                        })
                    }
                }
            } catch (e: Exception) {
                Log.e("Parameters", "Error loading parameters", e)
            }
        }
    }
}