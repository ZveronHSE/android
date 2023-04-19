package ru.zveron.lot_forms.data

import ru.zveron.models.lot_form.LotForm

class LotFormLocalSource: LotFormSource {
    private val cachedLotForms = mutableMapOf<Int, List<LotForm>>()
    private val allLotForms = mutableSetOf<LotForm>()

    override suspend fun loadLotForms(categoryId: Int): List<LotForm> {
        return cachedLotForms[categoryId] ?: emptyList()
    }

    fun saveLotForms(categoryId: Int, lotForms: List<LotForm>) {
        if (cachedLotForms[categoryId] != null) {
            return
        }
        cachedLotForms.put(categoryId, lotForms)
        allLotForms.addAll(lotForms)
    }

    fun getLotById(id: Int): LotForm? {
        return allLotForms.find { it.id == id }
    }
}