package ru.zveron.create_lot.first_step.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.categories.data.CategoryRepository
import ru.zveron.categories.data.CategorySelection
import ru.zveron.create_lot.data.LotCreateSelectedCategoriesRepository
import ru.zveron.create_lot.domain.LotCreateUploadPhotoInteractor
import ru.zveron.design.wrappers.ListWrapper

class FirstStepViewModel(
    private val categoryRepository: CategoryRepository,
    private val lotCreateSelectedCategoriesRepository: LotCreateSelectedCategoriesRepository,
    private val lotCreateUploadPhotoInteractor: LotCreateUploadPhotoInteractor,
) : ViewModel() {
    private val _rootCategoriesUiState =
        MutableStateFlow<RootCategoriesUiState>(RootCategoriesUiState.Loading)
    val rootCategoriesUiState = _rootCategoriesUiState.asStateFlow()

    private val _photoUploadUiState = MutableStateFlow(PhotoUploadUiState(ListWrapper(emptyList())))
    val photoUploadUiState = _photoUploadUiState.asStateFlow()

    val nameInputState = mutableStateOf("")
    private val nameInputStateFlow = snapshotFlow { nameInputState.value }

    val continueButtonState = combine(
        lotCreateSelectedCategoriesRepository.currentCategorySelection,
        nameInputStateFlow,
    ) { categorySelection, nameInput ->
        val canContinue = validateContinueButton(categorySelection, nameInput)
        ContinueButtonState(canContinue)
    }.stateIn(viewModelScope, SharingStarted.Lazily, ContinueButtonState(false))

    init {
        loadRootCategories()
    }

    fun onRootCategoryClick(categoryId: Int) {
        lotCreateSelectedCategoriesRepository.selectRootCategory(categoryId)
        _rootCategoriesUiState.update {
            when (it) {
                RootCategoriesUiState.Loading -> it
                is RootCategoriesUiState.Success -> it.copy(selectedRootCategoryId = categoryId)
            }
        }
    }

    fun onAddPhotoClick() {

    }

    fun onPhotoRetryClick(photoId: Long) {

    }

    fun onContinueClick() {

    }

    fun onNameInputted(value: String) {
        nameInputState.value = value
    }

    private fun loadRootCategories() {
        viewModelScope.launch {
            try {
                _rootCategoriesUiState.update { RootCategoriesUiState.Loading }
                val rootCategories = categoryRepository.loadRootCategories()
                _rootCategoriesUiState.update {
                    RootCategoriesUiState.Success(
                        rootCategories = ListWrapper(
                            rootCategories.map { RootCategoryUiState(id = it.id, name = it.name) },
                        ),
                        selectedRootCategoryId = null,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("Lot create", "Error loading root categories", e)
            }
        }
    }

    private fun validateContinueButton(
        categorySelection: CategorySelection,
        nameInput: String,
    ): Boolean {
        return categorySelection.rootCategory != null && nameInput.isNotBlank()
    }
}