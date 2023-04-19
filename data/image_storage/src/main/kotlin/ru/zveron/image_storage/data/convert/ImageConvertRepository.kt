package ru.zveron.image_storage.data.convert

interface ImageConvertRepository {
    suspend fun convertImage(uri: String): ImageConvertResult?
}