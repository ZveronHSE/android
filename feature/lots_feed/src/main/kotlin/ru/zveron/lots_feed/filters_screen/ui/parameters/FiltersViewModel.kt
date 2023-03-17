package ru.zveron.lots_feed.filters_screen.ui.parameters

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.lots_feed.filters_screen.data.parameters.ParametersRepository
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersHolder

internal class FiltersViewModel(
    private val parametersRepository: ParametersRepository,
    private val filtersSelectedParametersHolder: FiltersSelectedParametersHolder,
    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
) : ViewModel() {
    private val _parametersUiState =
        MutableStateFlow<FiltersParametersUiState>(FiltersParametersUiState.Loading)
    val parametersUiState = _parametersUiState.asStateFlow()

    init {
        loadParameters()
    }

    fun loadParameters() {
        val currentCategory =
            filtersSelectedCategoryRepository.currentCategorySelection.value.getCurrentCategory() ?: return
        viewModelScope.launch(Dispatchers.IO) {
            _parametersUiState.update { FiltersParametersUiState.Loading }
            try {
                val parameters = parametersRepository.loadParameters(
                    currentCategory.id,
                    filtersSelectedLotFormRepository.currentLotForm.value?.id ?: 0,
                )

                filtersSelectedParametersHolder.updateParameters(parameters)

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