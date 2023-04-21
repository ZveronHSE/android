package ru.zveron.create_lot.categories_step

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zveron.categories.data.CategoryRepository
import ru.zveron.choose_item.ChooseItem
import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.choose_item.ChooseItemUiState
import ru.zveron.create_lot.CreateLotScopeDelegate
import ru.zveron.create_lot.data.LotCreateSelectedCategoriesRepository
import ru.zveron.design.resources.ZveronText

internal class CategoriesItemProvider(
    private val scopeDelegate: CreateLotScopeDelegate,
    private val categoryRepository: CategoryRepository,
    private val lotCreateSelectedCategoriesRepository: LotCreateSelectedCategoriesRepository,
    private val categoriesStepNavigator: CategoriesStepNavigator,
) : ChooseItemItemProvider {
    private val _uiState = MutableStateFlow<ChooseItemUiState>(ChooseItemUiState.Loading)
    override val uiState: StateFlow<ChooseItemUiState> = _uiState.asStateFlow()

    override fun itemPicked(id: Int) {
        lotCreateSelectedCategoriesRepository.selectCategory(id)
        categoriesStepNavigator.completeCategoriesStep()
    }

    fun launchChildCategoryLoad() {
        scopeDelegate.coroutineScope.launch {
            try {
                val rootCategoryId =
                    lotCreateSelectedCategoriesRepository.currentCategorySelection.value.rootCategory?.id
                        ?: return@launch
                val categories = withContext(Dispatchers.IO) {
                    categoryRepository.loadCategoryChildren(rootCategoryId)
                }

                _uiState.update {
                    val items = categories.map {
                        ChooseItem(it.id, ZveronText.RawString(it.name))
                    }
                    ChooseItemUiState.Success(items)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("Lot create", "Error loading child categories", e)
            }
        }
    }
}