package ru.zveron.create_lot.domain

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zveron.create_lot.CreateLotScopeDelegate
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.create_lot.data.LotCreatePhotoStateRepository
import ru.zveron.create_lot.data.PhotoConversionCacheRepository
import ru.zveron.create_lot.data.PhotoUploadStatus
import ru.zveron.image_storage.data.convert.ImageConvertRepository
import ru.zveron.image_storage.data.convert.ImageConvertResult
import ru.zveron.image_storage.data.upload.ImageUploadRepository
import ru.zveron.image_storage.data.upload.ImageUploadSource

class LotCreateUploadPhotoInteractor(
    private val scopeDelegate: CreateLotScopeDelegate,
    private val lotCreatePhotoStateRepository: LotCreatePhotoStateRepository,
    private val imageConvertRepository: ImageConvertRepository,
    private val imageUploadRepository: ImageUploadRepository,
    private val photoConversionCacheRepository: PhotoConversionCacheRepository,
    private val lotCreateInfoRepository: LotCreateInfoRepository,
) {
    fun launchUploadPhoto(uri: String) {
        val id = lotCreatePhotoStateRepository.addNewPhoto(uri)
        scopeDelegate.coroutineScope.launch { launchUploadConvertedPhoto(id, uri) }
    }

    fun launchRetryPhotoUpload(id: String) {
        val uri = lotCreatePhotoStateRepository.getUri(id) ?: return
        val convertResult = photoConversionCacheRepository.getConversionResult(id)

        scopeDelegate.coroutineScope.launch { launchUploadConvertedPhoto(id, uri, convertResult) }
    }

    private suspend fun launchUploadConvertedPhoto(
        id: String,
        uri: String,
        convertResult: ImageConvertResult? = null,
    ) {
        withContext(Dispatchers.IO) {
            try {
                lotCreatePhotoStateRepository.updateStateForId(id, PhotoUploadStatus.LOADING)
                val actualConvertResult = convertResult ?: convertImage(id, uri)

                val url = imageUploadRepository.uploadImage(
                    actualConvertResult.bytes,
                    ImageUploadSource.LOT,
                    actualConvertResult.mimeType,
                )
                lotCreateInfoRepository.addPhotoUrl(url)

                lotCreatePhotoStateRepository.updateStateForId(id, PhotoUploadStatus.SUCCESS)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                lotCreatePhotoStateRepository.updateStateForId(id, PhotoUploadStatus.ERROR)
            }
        }
    }

    private suspend fun convertImage(id: String, uri: String): ImageConvertResult {
        val result = imageConvertRepository.convertImage(uri) ?: throw IllegalArgumentException("error converting image")
        photoConversionCacheRepository.saveConversionResult(id, result)
        return result
    }
}