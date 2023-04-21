package ru.zveron.image_storage.data.upload

import ru.zveron.contract.objectstorage.external.FlowSource

enum class ImageUploadSource {
    ORDER,
    PROFILE,
    LOT,
    CHAT,
}

internal fun ImageUploadSource.toGrpcFlowSource(): FlowSource {
    return when (this) {
        ImageUploadSource.ORDER -> FlowSource.ORDER
        ImageUploadSource.PROFILE -> FlowSource.PROFILE
        ImageUploadSource.LOT -> FlowSource.LOT
        ImageUploadSource.CHAT -> FlowSource.CHAT
    }
}