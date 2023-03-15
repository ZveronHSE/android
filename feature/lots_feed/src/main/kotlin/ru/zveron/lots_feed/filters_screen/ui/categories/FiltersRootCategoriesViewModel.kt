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
import ru.zveron.lots_feed.categories.data.CategoryRepository
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryHolder
import ru.zveron.lots_feed.filters_screen.domain.FiltersSetSelectedCategoryInteractor

class FiltersRootCategoriesViewModel(
    private val categoryRepository: CategoryRepository,
    private val filtersSelectedCategoryHolder: FiltersSelectedCategoryHolder,
    private val filtersSetSelectedCategoryInteractor: FiltersSetSelectedCategoryInteractor,
): ViewModel() {
    private val _rootCategoriesUiState = MutableStateFlow<RootCategoriesUiState>(RootCategoriesUiState.Loading)
    val rootCategoriesState = _rootCategoriesUiState.asStateFlow()

    init {
        loadRootCategories()

        filtersSelectedCategoryHolder
            .currentCategorySelection
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
    }

    private fun loadRootCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _rootCategoriesUiState.update { RootCategoriesUiState.Loading }
            try {
                val categories = categoryRepository.loadRootCategories()
                _rootCategoriesUiState.update {
                    RootCategoriesUiState.Success(
                        categories = categories.map { RootCategoryUiState(it.id, it.name) },
                        selectedCategoryId = filtersSelectedCategoryHolder.currentCategorySelection.value.rootCategory?.id,
                    )
                }
            } catch (e: Exception) {
                Log.e("Root categories", "Error loading root categories", e)
            }
        }
    }

    fun rootCategorySelected(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            filtersSetSelectedCategoryInteractor.setRootCategory(categoryId)
        }
    }
}