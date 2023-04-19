package ru.zveron.create_lot.lot_form

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zveron.choose_item.ChooseItem
import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.choose_item.ChooseItemUiState
import ru.zveron.create_lot.CreateLotScopeDelegate
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.create_lot.data.LotCreateSelectedCategoriesRepository
import ru.zveron.lot_forms.data.LotFormRepository

internal class LotFormItemProvider(
    private val lotCreateInfoRepository: LotCreateInfoRepository,
    private val lotFormRepository: LotFormRepository,
    private val scopeDelegate: CreateLotScopeDelegate,
    private val lotCreateSelectedCategoriesRepository: LotCreateSelectedCategoriesRepository,
    private val lotFormStepNavigator: LotFormStepNavigator,
): ChooseItemItemProvider {
    private val _uiState = MutableStateFlow<ChooseItemUiState>(ChooseItemUiState.Loading)
    override val uiState: StateFlow<ChooseItemUiState> = _uiState.asStateFlow()

    override fun itemPicked(id: Int) {
        val lotForm = lotFormRepository.getLotFormById(id)
        lotCreateInfoRepository.setLorForm(lotForm)

        lotFormStepNavigator.completeLotFormStep()
    }

    fun launchLotFormLoad() {
        scopeDelegate.coroutineScope.launch {
            try {
                val rootCategoryId =
                    lotCreateSelectedCategoriesRepository.currentCategorySelection.value.rootCategory?.id
                        ?: return@launch

                val lotForms = withContext(Dispatchers.IO) {
                    lotFormRepository.loadLotForms(rootCategoryId)
                }

                _uiState.update {
                    val items = lotForms.map {
                        ChooseItem(it.id, it.title)
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