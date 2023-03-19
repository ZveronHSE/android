package ru.zveron.lots_feed.feed.data.lot_forms

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.lots_feed.lot_forms.data.LotFormRepository
import ru.zveron.lots_feed.models.lot_form.LotForm

class SelectedLotFormRepository(
    private val lotFormRepository: LotFormRepository,
) {
    private val _currentLotForm = MutableStateFlow<LotForm?>(null)
    val currentLotForm = _currentLotForm.asStateFlow()

    fun selectLotForm(lotFormId: Int) {
        val lotForm = lotFormRepository.getLotFormById(lotFormId)
        _currentLotForm.update { lotForm }
    }

    fun resetLotForm() {
        _currentLotForm.update { null }
    }
}