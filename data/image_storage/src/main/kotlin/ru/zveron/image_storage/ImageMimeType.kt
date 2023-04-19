package ru.zveron.image_storage

import ru.zveron.contract.objectstorage.external.MimeType as GrpcMimeType

enum class ImageUploadMimeType {
    JPEG,
    PNG,
}


internal fun ImageUploadMimeType.toGrpcMimeType(): GrpcMimeType {
    return when (this) {
        ImageUploadMimeType.JPEG -> GrpcMimeType.IMAGE_JPEG
        ImageUploadMimeType.PNG -> GrpcMimeType.IMAGE_PNG
    }
}