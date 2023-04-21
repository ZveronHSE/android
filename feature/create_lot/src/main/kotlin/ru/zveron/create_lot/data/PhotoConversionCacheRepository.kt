package ru.zveron.create_lot.data

import ru.zveron.image_storage.data.convert.ImageConvertResult

internal class PhotoConversionCacheRepository {
    private val conversionMap = mutableMapOf<String, ImageConvertResult>()

    fun saveConversionResult(id: String, conversionResult: ImageConvertResult) {
        conversionMap[id] = conversionResult
    }

    fun getConversionResult(id: String): ImageConvertResult? {
        return conversionMap[id]
    }
}