package ru.zveron.lots_feed.filters_screen.ui.parameters

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zveron.choose_item.ChooseItemHolder
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.filters_screen.FiltersNavigator
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersParametersLoadingRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.parameters.ParameterState
import ru.zveron.lots_feed.filters_screen.domain.parameters.FiltersParameterItemProviderFactory
import ru.zveron.lots_feed.filters_screen.domain.parameters.FiltersUpdateParametersInteractor
import ru.zveron.parameters.data.ParametersRepository

internal class FiltersViewModel(
    private val filtersUpdateParametersInteractor: FiltersUpdateParametersInteractor,
    filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersParameterItemProviderFactory: FiltersParameterItemProviderFactory,
    private val filtersNavigator: FiltersNavigator,
    private val chooseItemHolder: ChooseItemHolder,
    private val parametersRepository: ParametersRepository,
    filtersSelectedParametersRepository: FiltersSelectedParametersRepository,
    private val filtersParametersLoadingRepository: FiltersParametersLoadingRepository,
) : ViewModel() {
    val parametersUiState = combine(
        filtersParametersLoadingRepository.isLoadingFlow,
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
            filtersParametersLoadingRepository.updateIsLoading(true)
            try {
                filtersUpdateParametersInteractor.loadParameters()
                filtersParametersLoadingRepository.updateIsLoading(false)
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
        val itemProvider = filtersParameterItemProviderFactory.createItemProvider(id)

        chooseItemHolder.setCurrentItemItemProvider(itemProvider)
        filtersNavigator.chooseItem(ZveronText.RawString(parameter.name))
    }
}