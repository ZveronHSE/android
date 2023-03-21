package ru.zveron.lots_feed.filters_screen.ui.categories

import androidx.compose.runtime.Immutable
import ru.zveron.design.resources.ZveronText

@Immutable
sealed class ChildrenCategoriesUiState {
    @Immutable
    object Hidden: ChildrenCategoriesUiState()

    @Immutable
    data class Show(val selectedChildCategoryTitle: ZveronText): ChildrenCategoriesUiState()
}