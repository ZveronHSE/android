package ru.zveron.create_lot.first_step.ui

import androidx.compose.runtime.Immutable
import ru.zveron.design.components.PhotoUploadState
import ru.zveron.design.wrappers.ListWrapper

@Immutable
sealed class RootCategoriesUiState {
    data class Success(
        val rootCategories: ListWrapper<RootCategoryUiState>,
        val selectedRootCategoryId: Int?,
    ): RootCategoriesUiState()

    object Loading: RootCategoriesUiState()
}

data class RootCategoryUiState(
    val id: Int,
    val name: String,
)

data class PhotoUploadUiState(
    val states: ListWrapper<Pair<String, PhotoUploadState>>,
)

data class ContinueButtonState(
    val isEnabled: Boolean,
)