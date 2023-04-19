package ru.zveron.image_storage.domain

import ru.zveron.image_storage.data.convert.ImageConvertRepository
import ru.zveron.image_storage.data.upload.ImageUploadRepository
import ru.zveron.image_storage.data.upload.ImageUploadSource

internal class UploadImageInteractorImpl(
    private val imageConvertRepository: ImageConvertRepository,
    private val imageUploadRepository: ImageUploadRepository,
) : UploadImageInteractor {
    override suspend fun uploadImage(
        fileImageUri: String,
        imageUploadSource: ImageUploadSource
    ): String {
        val convertResult = imageConvertRepository.convertImage(fileImageUri)
            ?: throw IllegalArgumentException("error converting uri $fileImageUri")

        return imageUploadRepository.uploadImage(
            convertResult.bytes,
            imageUploadSource,
            convertResult.mimeType,
        )
    }
}