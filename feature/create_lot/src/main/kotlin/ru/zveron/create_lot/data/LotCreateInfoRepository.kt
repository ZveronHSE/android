package ru.zveron.create_lot.data

import ru.zveron.models.lot_form.LotForm

internal class LotCreateInfoRepository {
    private val photoUrls = mutableListOf<String>()
    private var lotName: String = ""

    private var lotForm: LotForm? = null

    fun setName(name: String) {
        lotName = name
    }

    fun addPhotoUrl(url: String) {
        photoUrls.add(url)
    }

    fun setLorForm(lotForm: LotForm) {
        this.lotForm = lotForm
    }
}