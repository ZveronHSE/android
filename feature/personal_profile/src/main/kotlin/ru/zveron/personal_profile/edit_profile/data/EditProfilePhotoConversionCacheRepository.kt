package ru.zveron.personal_profile.edit_profile.data

import ru.zveron.image_storage.data.convert.ImageConvertResult

class EditProfilePhotoConversionCacheRepository {
    private val conversionMap = mutableMapOf<String, ImageConvertResult>()

    fun saveConversionResult(uri: String, conversionResult: ImageConvertResult) {
        conversionMap[uri] = conversionResult
    }

    fun getConversionResult(uri: String): ImageConvertResult? {
        return conversionMap[uri]
    }
}