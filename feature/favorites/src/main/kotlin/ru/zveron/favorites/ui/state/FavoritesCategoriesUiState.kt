package ru.zveron.favorites.ui.state

sealed class FavoritesCategoriesUiState {
    object Loading: FavoritesCategoriesUiState()

    data class Success(
        val categories: List<CategoryUiState>,
        val selectedCategoryId: Int,
    ): FavoritesCategoriesUiState()
}

data class CategoryUiState(
    val id: Int,
    val title: String,
)