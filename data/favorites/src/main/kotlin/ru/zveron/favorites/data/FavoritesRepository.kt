package ru.zveron.favorites.data

import com.google.protobuf.Empty
import ru.zveron.favorites.lot.AddLotToFavoritesRequest
import ru.zveron.favorites.lot.GetFavoriteLotsRequest
import ru.zveron.favorites.lot.GetFavoriteLotsResponse
import ru.zveron.favorites.lot.RemoveLotFromFavoritesRequest
import ru.zveron.favorites.lot.addLotToFavoritesRequest
import ru.zveron.favorites.lot.getFavoriteLotsRequest
import ru.zveron.favorites.lot.removeLotFromFavoritesRequest
import ru.zveron.models.lots.Lot
import ru.zveron.models.mappings.toDomainLot
import ru.zveron.network.ApigatewayDelegate

private const val GET_FAVORITE_LOTS_METHOD_NAME = "lotFavoritesGet"
private const val ADD_LOT_TO_FAVORITES_METHOD_NAME = "lotFavoritesAdd"
private const val REMOVE_LOT_TO_FAVORITES_METHOD_NAME = "lotFavoritesDelete"

class FavoritesRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun getFavoriteLots(
        categoryId: Int,
    ): List<Lot> {
        val request = getFavoriteLotsRequest {
            this.categoryId = categoryId
        }

        val response = apigatewayDelegate.callApiGateway<GetFavoriteLotsRequest, GetFavoriteLotsResponse>(
            GET_FAVORITE_LOTS_METHOD_NAME,
            request,
            GetFavoriteLotsResponse.newBuilder(),
        )

        return response.favoriteLotsList.map { it.toDomainLot() }
    }

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