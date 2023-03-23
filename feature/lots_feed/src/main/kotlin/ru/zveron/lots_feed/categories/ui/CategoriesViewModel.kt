package ru.zveron.lots_feed.categories.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.categories.data.CategorySelection
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.R
import ru.zveron.lots_feed.categories.domain.CategoryInteractor
import ru.zveron.lots_feed.categories.domain.SelectedCategoriesInteractor
import ru.zveron.lots_feed.mappings.toUiState

internal class CategoriesViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val selectedCategoriesInteractor: SelectedCategoriesInteractor,
): ViewModel() {
    private val _uiState = MutableStateFlow<CategoriesUiState>(
        CategoriesUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    val categoryTitle = selectedCategoriesInteractor.currentCategorySelection
        .map { categorySelection ->
            categorySelection.getCurrentCategory()?.let {
                ZveronText.RawString(it.name)
            } ?: ZveronText.RawResource(R.string.root_category_title)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, ZveronText.RawResource(R.string.root_category_title))

    val canNavigateBack = selectedCategoriesInteractor.currentCategorySelection
        .map { categorySelection -> !categorySelection.isEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)


    init {
        selectedCategoriesInteractor.currentCategorySelection
            .onEach(::categorySelectionUpdated)
            .launchIn(viewModelScope)

        loadCategories(null)
    }

    private fun categorySelectionUpdated(categorySelection: CategorySelection) {
        if (categorySelection.innerCategory == null) {
            loadCategories(categorySelection.getCurrentCategory()?.id)
        } else {
            _uiState.update { CategoriesUiState.Hidden }
        }
    }

    private fun loadCategories(categoryId: Int?) {
        viewModelScope.launch {
            try {
                _uiState.update{ CategoriesUiState.Loading }
                val categories = categoryInteractor.loadChildrenCategories(categoryId)
                _uiState.update { CategoriesUiState.Success(categories.map { it.toUiState() }) }
            } catch (e: Exception) {
                Log.e("Categories", "Error loading child categories", e)
            }
        }
    }

    fun onCategoryClicked(categoryId: Int) {
        selectedCategoriesInteractor.setNextLevelCategory(categoryId)
    }
}