package ru.zveron.personal_profile.edit_profile.domain

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zveron.image_storage.data.convert.ImageConvertRepository
import ru.zveron.image_storage.data.convert.ImageConvertResult
import ru.zveron.image_storage.data.upload.ImageUploadRepository
import ru.zveron.image_storage.data.upload.ImageUploadSource
import ru.zveron.personal_profile.edit_profile.data.EditProfilePhotoConversionCacheRepository
import ru.zveron.personal_profile.edit_profile.data.EditProfilePhotoStateRepository
import ru.zveron.personal_profile.edit_profile.data.EditProfilePhotoUriRepository
import ru.zveron.personal_profile.edit_profile.data.PhotoUploadStatus

internal class EditProfileUploadAvatarInteractor(
    private val editProfilePhotoUriRepository: EditProfilePhotoUriRepository,
    private val imageConvertRepository: ImageConvertRepository,
    private val editProfilePhotoConversionCacheRepository: EditProfilePhotoConversionCacheRepository,
    private val imageUploadRepository: ImageUploadRepository,
    private val editProfilePhotoStateRepository: EditProfilePhotoStateRepository,
) {
    suspend fun uploadPhoto(uri: String) {
        editProfilePhotoUriRepository.saveUri(uri)
        uploadConvertedPhoto(uri)
    }

    suspend fun retryAvatarUpload() {
        val uri = editProfilePhotoUriRepository.getCurrentUri() ?: throw IllegalArgumentException("no image was uploaded before")
        val convertResult = editProfilePhotoConversionCacheRepository.getConversionResult(uri)

        uploadConvertedPhoto(uri, convertResult)
    }

    private suspend fun uploadConvertedPhoto(
        uri: String,
        convertResult: ImageConvertResult? = null,
    ) {
        withContext(Dispatchers.IO){
            try {
                editProfilePhotoStateRepository.updateState(PhotoUploadStatus.LOADING, uri)
                val actualConvertResult = convertResult ?: convertImage(uri)
                val photoUrl = imageUploadRepository.uploadImage(
                    bytes = actualConvertResult.bytes,
                    mimeType = actualConvertResult.mimeType,
                    imageSource = ImageUploadSource.PROFILE,
                )
                editProfilePhotoStateRepository.updateState(PhotoUploadStatus.SUCCESS, uri, photoUrl)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("Personal profile edit", "Error uploading photo", e)
                editProfilePhotoStateRepository.updateState(PhotoUploadStatus.ERROR, uri)
            }
        }
    }

    private suspend fun convertImage(uri: String): ImageConvertResult {
        val result = imageConvertRepository.convertImage(uri) ?: throw IllegalArgumentException("error converting image")
        editProfilePhotoConversionCacheRepository.saveConversionResult(uri, result)
        return result
    }
}