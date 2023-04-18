package ru.zveron.create_lot.first_step.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zveron.categories.data.CategoryRepository
import ru.zveron.design.wrappers.ListWrapper

class FirstStepViewModel(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _rootCategoriesUiState =
        MutableStateFlow<RootCategoriesUiState>(RootCategoriesUiState.Loading)
    val rootCategoriesUiState = _rootCategoriesUiState.asStateFlow()

    private val _photoUploadUiState = MutableStateFlow(PhotoUploadUiState(ListWrapper(emptyList())))
    val photoUploadUiState = _photoUploadUiState.asStateFlow()

    val nameInputState = mutableStateOf("")

    private val _continueButtonUiState = MutableStateFlow(ContinueButtonState(false))
    val continueButtonState = _continueButtonUiState.asStateFlow()

    fun onRootCategoryClick(categoryId: Int) {

    }

    fun onAddPhotoClick() {

    }

    fun onPhotoRetryClick(photoId: Long) {

    }

    fun onContinueClick() {

    }

    fun onNameInputted(value: String) {

    }
}