package ru.zveron.lots_feed.filters_screen.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.lots_feed.filters_screen.FiltersParams
import ru.zveron.lots_feed.filters_screen.data.ParametersRepository
import ru.zveron.lots_feed.filters_screen.domain.ParametersHolder

internal class FiltersViewModel(
    private val filtersParams: FiltersParams,
    private val parametersRepository: ParametersRepository,
    private val parametersHolder: ParametersHolder,
) : ViewModel() {
    private val _parametersUiState =
        MutableStateFlow<FiltersParametersUiState>(FiltersParametersUiState.Loading)
    val parametersUiState = _parametersUiState.asStateFlow()

    fun loadParameters() {
        viewModelScope.launch(Dispatchers.IO) {
            _parametersUiState.update { FiltersParametersUiState.Loading }
            try {
                val parameters = parametersRepository.loadParameters(
                    filtersParams.selectedCategoryId,
                    filtersParams.selectedLotFormId,
                )

                parametersHolder.updateParameters(parameters)

                _parametersUiState.update {
                    FiltersParametersUiState.Success(parameters.map {
                        ParameterUiState(it.id, it.name, false)
                    })
                }
            } catch (e: Exception) {
                Log.e("Parameters", "Error loading parameters", e)
            }
        }
    }
}