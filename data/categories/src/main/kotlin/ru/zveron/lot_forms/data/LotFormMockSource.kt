package ru.zveron.lot_forms.data

import kotlinx.coroutines.delay
import ru.zveron.models.lot_form.LotForm

class LotFormMockSource: LotFormSource {
    override suspend fun loadLotForms(categoryId: Int): List<LotForm> {
        delay(2000L)
        return listOf(
            LotForm(1, "На случку"),
            LotForm(2, "На продажу"),
        )
    }
}