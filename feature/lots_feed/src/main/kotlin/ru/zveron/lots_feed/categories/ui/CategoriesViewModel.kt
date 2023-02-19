package ru.zveron.lots_feed.categories.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.zveron.lots_feed.categories.domain.CategoryInteractor
import ru.zveron.lots_feed.mappings.toUiState

internal class CategoriesViewModel(
    private val categoryInteractor: CategoryInteractor,
): ViewModel() {
    private val _uiState = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadCategories(categoryId: Int?) {
        viewModelScope.launch {
            _uiState.value = CategoriesUiState.Loading
            val categories = categoryInteractor.loadChildrenCategories(categoryId)
            _uiState.value = CategoriesUiState.Success(categories.map { it.toUiState() })
        }
    }
}