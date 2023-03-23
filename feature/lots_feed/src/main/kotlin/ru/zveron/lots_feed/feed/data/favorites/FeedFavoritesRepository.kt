package ru.zveron.lots_feed.feed.data.favorites

import com.google.protobuf.Empty
import ru.zveron.favorites.lot.AddLotToFavoritesRequest
import ru.zveron.favorites.lot.RemoveLotFromFavoritesRequest
import ru.zveron.favorites.lot.addLotToFavoritesRequest
import ru.zveron.favorites.lot.removeLotFromFavoritesRequest
import ru.zveron.network.ApigatewayDelegate

private const val ADD_LOT_TO_FAVORITES_METHOD_NAME = "lotFavoritesAdd"
private const val REMOVE_LOT_TO_FAVORITES_METHOD_NAME = "lotFavoritesDelete"

class FeedFavoritesRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun addLotToFavorites(lotId: Long) {
        val request = addLotToFavoritesRequest {
            this.id = lotId
        }

        apigatewayDelegate.callApiGateway<AddLotToFavoritesRequest, Empty>(
            ADD_LOT_TO_FAVORITES_METHOD_NAME,
            request,
            Empty.newBuilder(),
        )
    }

    suspend fun removeLotFromFavorites(lotId: Long) {
        val request = removeLotFromFavoritesRequest {
            this.id = lotId
        }

        apigatewayDelegate.callApiGateway<RemoveLotFromFavoritesRequest, Empty>(
            REMOVE_LOT_TO_FAVORITES_METHOD_NAME,
            request,
            Empty.newBuilder(),
        )
    }
}