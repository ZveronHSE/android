package ru.zveron.create_lot.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.models.lot_form.LotForm

internal class LotCreateInfoRepository {
    private val photoUrls = mutableListOf<String>()
    private var lotName: String = ""

    private var lotForm: LotForm? = null

    private val _selectedParametersState = MutableStateFlow<Map<Int, String>>(emptyMap())
    val selectedParametersState = _selectedParametersState.asStateFlow()

    fun setName(name: String) {
        lotName = name
    }

    fun addPhotoUrl(url: String) {
        photoUrls.add(url)
    }

    fun getLotFormId(): Int? {
        return lotForm?.id
    }

    fun setLorForm(lotForm: LotForm) {
        this.lotForm = lotForm
    }

    fun updateParameterValue(id: Int, value: String) {
        _selectedParametersState.update { currentMap ->
            buildMap {
                putAll(currentMap)
                put(id, value)
            }
        }
    }

    fun getParameterValue(id: Int): String? {
        return _selectedParametersState.value[id]
    }
}