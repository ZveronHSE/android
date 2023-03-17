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
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.choose_item.ChooseItemHolder
import ru.zveron.lots_feed.filters_screen.FiltersNavigator
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.ParameterState
import ru.zveron.lots_feed.filters_screen.domain.parameters.FiltersUpdateParametersInteractor
import ru.zveron.lots_feed.filters_screen.domain.parameters.ParameterItemProviderFactory
import ru.zveron.lots_feed.parameters.data.ParametersRepository

internal class FiltersViewModel(
    private val filtersUpdateParametersInteractor: FiltersUpdateParametersInteractor,
    filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val parameterItemProviderFactory: ParameterItemProviderFactory,
    private val filtersNavigator: FiltersNavigator,
    private val chooseItemHolder: ChooseItemHolder,
    private val parametersRepository: ParametersRepository,
    filtersSelectedParametersRepository: FiltersSelectedParametersRepository,
) : ViewModel() {
    private val _parametersLoadingUiState = MutableStateFlow(true)

    val parametersUiState = combine(
        _parametersLoadingUiState,
        filtersSelectedCategoryRepository.currentCategorySelection,
        filtersSelectedParametersRepository.parametersState
    ) { isLoading, categorySelection, parametersSelection ->
        when {
            categorySelection.innerCategory == null -> FiltersParametersUiState.Hidden
            isLoading -> FiltersParametersUiState.Loading
            parametersSelection.isEmpty() -> FiltersParametersUiState.Hidden
            else -> FiltersParametersUiState.Success(mapParameterState(parametersSelection))
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, FiltersParametersUiState.Hidden)

    init {
        filtersUpdateParametersInteractor.updateFlow
            .onEach { loadParameters() }
            .launchIn(viewModelScope)
    }

    private fun loadParameters() {
        viewModelScope.launch(Dispatchers.IO) {
            _parametersLoadingUiState.update { true }
            try {
                filtersUpdateParametersInteractor.loadParameters()
                _parametersLoadingUiState.update { false }
            } catch (e: Exception) {
                Log.e("Parameters", "Error loading parameters", e)
            }
        }
    }

    private fun mapParameterState(parametersSelection: List<ParameterState>): List<ParameterUiState> {
        return parametersSelection.map {
            val title = it.value ?: it.parameter.name
            ParameterUiState(it.parameter.id, title, it.value != null)
        }
    }

    fun onParameterRowClicked(id: Int) {
        val parameter = parametersRepository.getParameterById(id)
        val itemProvider = parameterItemProviderFactory.createItemProvider(id)

        chooseItemHolder.setCurrentItemItemProvider(itemProvider)
        filtersNavigator.chooseItem(ZveronText.RawString(parameter.name))
    }
}