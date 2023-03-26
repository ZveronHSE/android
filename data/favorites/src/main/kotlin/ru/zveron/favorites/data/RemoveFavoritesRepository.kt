package ru.zveron.favorites.data

import com.google.protobuf.Empty
import ru.zveron.favorites.lot.DeleteAllByCategoryRequest
import ru.zveron.favorites.lot.DeleteAllByStatusAndCategoryRequest
import ru.zveron.favorites.lot.deleteAllByCategoryRequest
import ru.zveron.favorites.lot.deleteAllByStatusAndCategoryRequest
import ru.zveron.models.lots.Status
import ru.zveron.models.mappings.toGrpcStatus
import ru.zveron.network.ApigatewayDelegate

private const val REMOVE_BY_CATEGORY_METHOD_NAME = "lotFavoritesDeleteByCategory"
private const val REMOVE_BY_STATUS_AND_CATEGORY_METHOD_NAME = "lotFavoritesDeleteByStatusAndCategory"

class RemoveFavoritesRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun removeAllByCategory(
        categoryId: Int,
    ) {
        val request = deleteAllByCategoryRequest {
            this.categoryId = categoryId
        }

        apigatewayDelegate.callApiGateway<DeleteAllByCategoryRequest, Empty>(
            REMOVE_BY_CATEGORY_METHOD_NAME,
            request,
            Empty.newBuilder(),
        )
    }

    suspend fun removeAllByCategoriesAndStatus(
        categoryId: Int,
        status: Status
    ) {
        val request = deleteAllByStatusAndCategoryRequest {
            this.categoryId = categoryId
            this.status = status.toGrpcStatus()
        }

        apigatewayDelegate.callApiGateway<DeleteAllByStatusAndCategoryRequest, Empty>(
            REMOVE_BY_STATUS_AND_CATEGORY_METHOD_NAME,
            request,
            Empty.newBuilder(),
        )
    }
}