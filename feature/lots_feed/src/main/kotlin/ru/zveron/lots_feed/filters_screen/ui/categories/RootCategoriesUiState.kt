package ru.zveron.lots_feed.filters_screen.ui.categories

import androidx.compose.runtime.Immutable

@Immutable
sealed class RootCategoriesUiState {
    @Immutable
    object Loading: RootCategoriesUiState()

    @Immutable
    data class Success(
        val categories: List<RootCategoryUiState>,
        val selectedCategoryId: Int?,
    ): RootCategoriesUiState()
}

@Immutable
data class RootCategoryUiState(
    val id: Int,
    val name: String,
)