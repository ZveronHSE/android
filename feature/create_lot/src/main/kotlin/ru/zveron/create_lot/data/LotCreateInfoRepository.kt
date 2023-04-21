package ru.zveron.create_lot.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.categories.data.CategorySelection
import ru.zveron.contract.lot.CreateLotRequest
import ru.zveron.contract.lot.createLotRequest
import ru.zveron.contract.lot.fullAddress
import ru.zveron.contract.lot.model.photo
import ru.zveron.models.communication_channels.CommunicationChannel
import ru.zveron.models.gender.Gender
import ru.zveron.models.lot_form.LotForm
import ru.zveron.models.mappings.toGrpcCommunicationChannel
import ru.zveron.models.mappings.toGrpcGender

internal class LotCreateInfoRepository {
    private val photoUrls = mutableListOf<String>()
    private var lotName: String? = null

    private var lotForm: LotForm? = null

    private var price: Int? = null

    private val _selectedParametersState = MutableStateFlow<Map<Int, String>>(emptyMap())
    val selectedParametersState = _selectedParametersState.asStateFlow()

    private var description: String? = null

    private var communicationChannels: List<CommunicationChannel>? = null

    private var address: String? = null

    private var gender: Gender? = null

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

    fun setDescription(description: String) {
        this.description = description
    }

    fun setPrice(price: Int) {
        this.price = price
    }

    fun setCommunicationChannels(channels: List<CommunicationChannel>) {
        this.communicationChannels = channels
    }

    fun setAddress(address: String) {
        this.address = address
    }

    fun setGender(gender: Gender) {
        this.gender = gender
    }

    fun buildRequest(
        categorySelection: CategorySelection,
    ): CreateLotRequest {
        require(categorySelection.rootCategory != null && categorySelection.innerCategory != null) {
            "category not set"
        }
        val name = this.lotName ?: throw IllegalStateException("name not set")
        val lotForm = this.lotForm ?: throw IllegalStateException("lot form not set")
        val price = this.price ?: throw IllegalStateException("price not set")
        val communicationChannels = this.communicationChannels
            ?: throw IllegalStateException("communication channels not set")
        val address = this.address ?: throw IllegalStateException("address not set")
        val description = this.description ?: throw IllegalStateException("description")
        val gender = this.gender

        return createLotRequest {
            this.title = name
            this.photos.addAll(
                photoUrls.mapIndexed { index, url ->
                    photo {
                        this.url = url
                        this.order = index
                    }
                }
            )
            this.parameters.putAll(this@LotCreateInfoRepository._selectedParametersState.value)
            this.description = description
            this.price = price

            // TODO: pass correct address here
            this.address = fullAddress {
                this.district = address
                this.latitude = 0.0
                this.longitude = 0.0
            }

            this.lotFormId = lotForm.id
            this.categoryId = categorySelection.innerCategory!!.id
            this.communicationChannel.addAll(communicationChannels.map { it.toGrpcCommunicationChannel() })

            gender?.let {
                this.gender = gender.toGrpcGender()
            }
        }
    }
}