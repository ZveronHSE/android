package ru.zveron.create_lot.domain

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zveron.create_lot.CreateLotScopeDelegate
import ru.zveron.create_lot.data.LotCreatePhotoStateRepository
import ru.zveron.create_lot.data.PhotoUploadStatus
import ru.zveron.image_storage.data.upload.ImageUploadSource
import ru.zveron.image_storage.domain.UploadImageInteractor

class LotCreateUploadPhotoInteractor(
    private val scopeDelegate: CreateLotScopeDelegate,
    private val lotCreatePhotoStateRepository: LotCreatePhotoStateRepository,
    private val uploadImageInteractor: UploadImageInteractor,
) {
    fun launchUploadPhoto(uri: String) {
        scopeDelegate.coroutineScope.launch {
            val id = lotCreatePhotoStateRepository.addNewPhoto(uri)

            withContext(Dispatchers.IO) {
                try {
                    lotCreatePhotoStateRepository.updateStateForId(id, PhotoUploadStatus.LOADING)
                    uploadImageInteractor.uploadImage(uri, ImageUploadSource.LOT)
                    lotCreatePhotoStateRepository.updateStateForId(id, PhotoUploadStatus.SUCCESS)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    lotCreatePhotoStateRepository.updateStateForId(id, PhotoUploadStatus.ERROR)
                }
            }
        }
    }
}