package ru.zveron.image_storage.domain

import ru.zveron.image_storage.data.upload.ImageUploadSource

interface UploadImageInteractor {
    suspend fun uploadImage(fileImageUri: String, imageUploadSource: ImageUploadSource): String
}