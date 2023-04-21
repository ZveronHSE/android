package ru.zveron.lot_forms.data

import ru.zveron.models.lot_form.LotForm


class LotFormRepository(
    private val lotFormSource: LotFormSource,
    private val lotFormLocalSource: LotFormLocalSource,
) {
    suspend fun loadLotForms(categoryId: Int): List<LotForm> {
        val localLotForms = lotFormLocalSource.loadLotForms(categoryId)
        if (localLotForms.isNotEmpty()) {
            return localLotForms
        }

        val remoteLotForms = lotFormSource.loadLotForms(categoryId)
        lotFormLocalSource.saveLotForms(categoryId, remoteLotForms)
        return remoteLotForms
    }

    fun getLotFormById(id: Int): LotForm {
        return lotFormLocalSource.getLotById(id)!!
    }
}