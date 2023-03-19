package ru.zveron.lots_feed.feed.ui.parameters

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
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.categories.domain.SelectedCategoriesInteractor
import ru.zveron.lots_feed.choose_item.ChooseItemHolder
import ru.zveron.lots_feed.feed.LotsFeedNavigator
import ru.zveron.lots_feed.feed.data.parameters.ParametersLoadingRepository
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.feed.domain.UpdateParametersInteractorImpl
import ru.zveron.lots_feed.feed.domain.parameters.ParameterItemProviderFactory
import ru.zveron.lots_feed.filters_screen.data.parameters.ParameterState
import ru.zveron.lots_feed.parameters.data.ParametersRepository

internal class ParametersViewModel(
    selectedCategoriesInteractor: SelectedCategoriesInteractor,
    selectedParametersRepository: SelectedParametersRepository,
    private val updateParametersInteractor: UpdateParametersInteractorImpl,
    private val parametersItemProviderFactory: ParameterItemProviderFactory,
    private val chooseItemHolder: ChooseItemHolder,
    private val lotsFeedNavigator: LotsFeedNavigator,
    private val parametersRepository: ParametersRepository,
    private val parametersLoadingRepository: ParametersLoadingRepository,
) : ViewModel() {
    val uiState = combine(
        parametersLoadingRepository.isLoadingFlow,
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

    fun parameterClicked(id: Int) {
        chooseItemHolder.setCurrentItemItemProvider(
            parametersItemProviderFactory.createItemProvider(id)
        )

        val title = parametersRepository.getParameterById(id).name
        lotsFeedNavigator.chooseItem(ZveronText.RawString(title))
    }

    private fun loadParameters() {
        viewModelScope.launch(Dispatchers.IO) {
            parametersLoadingRepository.updateIsLoading(true)
            try {
                updateParametersInteractor.loadParameters()
                parametersLoadingRepository.updateIsLoading(false)
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