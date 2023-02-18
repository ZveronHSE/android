package ru.zveron.lots_feed.categories.ui

import androidx.compose.runtime.Immutable
import ru.zveron.design.resources.ZveronImage

sealed class CategoriesUiState {
    object Loading: CategoriesUiState()

    data class Success(val categories: List<Category>): CategoriesUiState()
}

@Immutable
data class Category(
    val id: Long,
    val image: ZveronImage,
    val title: String,
)