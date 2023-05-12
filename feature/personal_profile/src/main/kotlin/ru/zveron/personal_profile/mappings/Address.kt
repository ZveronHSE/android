package ru.zveron.personal_profile.mappings

import ru.zveron.contract.profile.Address
import ru.zveron.contract.profile.address
import ru.zveron.personal_profile.AddressUiInfo
import ru.zveron.personal_profile.profile_preview.data.ProfileAddress

internal fun Address.toDomain(): ProfileAddress {
    return ProfileAddress(
        region = this.region,
        town = this.town,
        longitude = this.longitude,
        latitude = this.latitude,
    )
}

internal fun ProfileAddress.toGrpc(): Address {
    return address {
        this.region = this@toGrpc.region
        this.town = this@toGrpc.town
        this.longitude = this@toGrpc.longitude
        this.latitude = this@toGrpc.latitude
    }
}

internal fun AddressUiInfo.toDomain(): ProfileAddress {
    return ProfileAddress(
        region = this.region,
        town = this.town,
        longitude = this.longitude,
        latitude = this.latitude,
    )
}

internal fun ProfileAddress.toUiModel(): AddressUiInfo {
    return AddressUiInfo(
        region = this.region,
        town = this.town,
        longitude = this.longitude,
        latitude = this.latitude,
    )
}