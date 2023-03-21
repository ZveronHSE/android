package ru.zveron.lots_feed.filters_screen.ui.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.domain.categories.FiltersSetSelectedCategoryInteractor
import ru.zveron.lots_feed.filters_screen.domain.categories.FiltersUpdateCategoriesInteractor

internal class FiltersRootCategoriesViewModel(
    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersSetSelectedCategoryInteractor: FiltersSetSelectedCategoryInteractor,
    private val filtersUpdateCategoriesInteractor: FiltersUpdateCategoriesInteractor,
): ViewModel() {
    private val _rootCategoriesUiState = MutableStateFlow<RootCategoriesUiState>(RootCategoriesUiState.Loading)
    val rootCategoriesState = _rootCategoriesUiState.asStateFlow()

    init {
        loadRootCategories()

        filtersSelectedCategoryRepository.currentCategorySelection
            .onEach {  currentSelection ->
                _rootCategoriesUiState.update { currentState ->
                    if (currentState is RootCategoriesUiState.Success) {
                        currentState.copy(selectedCategoryId = currentSelection.rootCategory?.id)
                    } else {
                        currentState
                    }
                }
            }
            .launchIn(viewModelScope)

        filtersUpdateCategoriesInteractor.updateRootCategoriesFlow
            .onEach{ loadRootCategories() }
            .launchIn(viewModelScope)
    }

    private fun loadRootCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _rootCategoriesUiState.update { RootCategoriesUiState.Loading }
            try {
                val categories = filtersUpdateCategoriesInteractor.loadRootCategories()
                _rootCategoriesUiState.update {
                    RootCategoriesUiState.Success(
                        categories = categories.map { RootCategoryUiState(it.id, it.name) },
                        selectedCategoryId = filtersSelectedCategoryRepository.currentCategorySelection.value.rootCategory?.id,
                    )
                }
            } catch (e: Exception) {
                Log.e("Root categories", "Error loading root categories", e)
            }
        }
    }

    fun rootCategorySelected(categoryId: Int) {
        filtersSetSelectedCategoryInteractor.setRootCategory(categoryId)
    }
}