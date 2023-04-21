package ru.zveron.image_storage.data.upload

import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zveron.contract.objectstorage.external.UploadImageRequest
import ru.zveron.contract.objectstorage.external.UploadImageResponse
import ru.zveron.contract.objectstorage.external.uploadImageRequest
import ru.zveron.image_storage.ImageUploadMimeType
import ru.zveron.image_storage.toGrpcMimeType
import ru.zveron.network.ApigatewayDelegate

private const val UPLOAD_IMAGE_METHOD_NAME = "uploadImage"

internal class ImageUploadRepositoryImpl(
    private val apigatewayDelegate: ApigatewayDelegate,
): ImageUploadRepository {
    override suspend fun uploadImage(
        bytes: ByteArray,
        imageSource: ImageUploadSource,
        mimeType: ImageUploadMimeType
    ): String {
        return withContext(Dispatchers.IO) {
            val request = uploadImageRequest {
                this.body = ByteString.copyFrom(bytes)
                this.mimeType = mimeType.toGrpcMimeType()
                this.flowSource = imageSource.toGrpcFlowSource()
            }

            val response =
                apigatewayDelegate.callApiGateway<UploadImageRequest, UploadImageResponse>(
                    methodName = UPLOAD_IMAGE_METHOD_NAME,
                    body = request,
                    responseBuilder = UploadImageResponse.newBuilder(),
                )

            response.imageUrl
        }
    }
}