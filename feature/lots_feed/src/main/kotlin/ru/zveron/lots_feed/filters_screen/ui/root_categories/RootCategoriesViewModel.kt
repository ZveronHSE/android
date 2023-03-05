package ru.zveron.lots_feed.filters_screen.ui.root_categories

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
import ru.zveron.lots_feed.categories.data.SelectedCategoriesRepository

class RootCategoriesViewModel(
    private val selectedCategoriesRepository: SelectedCategoriesRepository,
    private val categoryRepository: CategoryRepository,
): ViewModel() {
    private val _rootCategoriesState = MutableStateFlow<RootCategoriesState>(RootCategoriesState.Loading)
    val rootCategoriesState = _rootCategoriesState.asStateFlow()

    init {
        selectedCategoriesRepository
            .currentCategorySelection
            .onEach {  currentSelection ->
                _rootCategoriesState.update { currentState ->
                    if (currentState is RootCategoriesState.Success) {
                        currentState.copy(selectedCategoryId = currentSelection.rootCategory?.id)
                    } else {
                        currentState
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun loadRootCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _rootCategoriesState.update { RootCategoriesState.Loading }
            try {
                val categories = categoryRepository.loadRootCategories()
                _rootCategoriesState.update {
                    RootCategoriesState.Success(
                        categories = categories.map { RootCategoryUiState(it.id, it.name) },
                        selectedCategoryId = selectedCategoriesRepository.currentCategorySelection.value.rootCategory?.id,
                    )
                }
            } catch (e: Exception) {
                Log.e("Root categories", "Error loading root categories", e)
            }
        }
    }

    fun rootCategorySelected(categoryId: Int) {
        val category = categoryRepository.getCategoryById(categoryId)
        selectedCategoriesRepository.setRootCategory(category)
    }
}