package ru.zveron.image_storage.data.convert

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zveron.image_storage.ImageUploadMimeType

internal class ImageConvertRepositoryImpl(
    private val context: Context,
): ImageConvertRepository {
    private val contentResolver by lazy { context.contentResolver }

    override suspend fun convertImage(uri: String): ImageConvertResult? {
        return withContext(Dispatchers.IO) {
            val imageUri = Uri.parse(uri)
            val bytes = contentResolver.openInputStream(imageUri)?.buffered().use { stream ->
                stream?.readBytes()
            }
            val type = toMimeType(contentResolver.getType(imageUri))

            bytes?.let {
                type?.let {
                    ImageConvertResult(type, bytes)
                }
            }
        }
    }

    private fun toMimeType(type: String?): ImageUploadMimeType? {
        return when (type) {
            "image/jpeg" -> ImageUploadMimeType.JPEG
            "image/png" -> ImageUploadMimeType.PNG
            else -> null
        }
    }
}