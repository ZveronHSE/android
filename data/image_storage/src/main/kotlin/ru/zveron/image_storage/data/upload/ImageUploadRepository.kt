package ru.zveron.image_storage.data.upload

import ru.zveron.image_storage.ImageUploadMimeType

interface ImageUploadRepository {
    suspend fun uploadImage(
        bytes: ByteArray,
        imageSource: ImageUploadSource,
        mimeType: ImageUploadMimeType,
    ): String
}