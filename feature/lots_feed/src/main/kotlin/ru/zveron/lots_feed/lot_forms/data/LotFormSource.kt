package ru.zveron.lots_feed.lot_forms.data

import ru.zveron.lots_feed.models.lot_form.LotForm

interface LotFormSource {
    suspend fun loadLotForms(categoryId: Int): List<LotForm>
}