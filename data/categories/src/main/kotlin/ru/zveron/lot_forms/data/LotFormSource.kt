package ru.zveron.lot_forms.data

import ru.zveron.models.lot_form.LotForm

interface LotFormSource {
    suspend fun loadLotForms(categoryId: Int): List<LotForm>
}