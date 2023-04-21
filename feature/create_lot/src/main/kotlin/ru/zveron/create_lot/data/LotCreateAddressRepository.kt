package ru.zveron.create_lot.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zveron.models.address.FullAddress

class LotCreateAddressRepository {
    private val _addressState = MutableStateFlow<FullAddress?>(null)
    val addressState = _addressState.asStateFlow()
}