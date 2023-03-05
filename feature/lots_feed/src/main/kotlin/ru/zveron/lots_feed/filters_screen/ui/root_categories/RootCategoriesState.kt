package ru.zveron.lots_feed.filters_screen.ui.root_categories

import androidx.compose.runtime.Immutable

@Immutable
sealed class RootCategoriesState {
    @Immutable
    object Loading: RootCategoriesState()

    @Immutable
    data class Success(
        val categories: List<RootCategoryUiState>,
        val selectedCategoryId: Int?,
    ): RootCategoriesState()
}

@Immutable
data class RootCategoryUiState(
    val id: Int,
    val name: String,
)