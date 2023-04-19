package ru.zveron.image_storage.data.convert

import ru.zveron.image_storage.ImageUploadMimeType

class ImageConvertResult(
    val mimeType: ImageUploadMimeType,
    val bytes: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageConvertResult

        if (mimeType != other.mimeType) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mimeType.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
