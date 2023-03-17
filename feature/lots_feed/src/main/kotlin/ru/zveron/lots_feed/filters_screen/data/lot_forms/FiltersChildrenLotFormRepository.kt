package ru.zveron.lots_feed.filters_screen.data.lot_forms

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.zveron.lots_feed.lot_forms.data.LotFormRepository
import ru.zveron.lots_feed.models.lot_form.LotForm

class FiltersChildrenLotFormRepository(
    private val lotFormRepository: LotFormRepository,
) {
    private val _childrenLotFormState = MutableStateFlow<ChildLotFormState>(ChildLotFormState.Loading)
    val childrenLotFormState = _childrenLotFormState.asStateFlow()

    suspend fun updateChildrenLotForms(categoryId: Int) {
        withContext(Dispatchers.IO) {
            try {
                _childrenLotFormState.update { ChildLotFormState.Loading }
                val lotForms = lotFormRepository.loadLotForms(categoryId)
                _childrenLotFormState.update { ChildLotFormState.Success(lotForms) }
            } catch (e: Exception) {
                Log.e("LotForm", "Error loading lot forms for category", e)
            }
        }
    }
}

sealed class ChildLotFormState {
    object Loading: ChildLotFormState()

    data class Success(val lotForms: List<LotForm>): ChildLotFormState()
}