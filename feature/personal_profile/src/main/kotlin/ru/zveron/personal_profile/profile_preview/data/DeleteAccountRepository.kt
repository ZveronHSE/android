package ru.zveron.personal_profile.profile_preview.data

import com.google.protobuf.Empty
import com.google.protobuf.empty
import ru.zveron.network.ApigatewayDelegate

private const val DELETE_PROFILE_METHOD_NAME = "profileDelete"

internal class DeleteAccountRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun deleteAccount() {
        apigatewayDelegate.callApiGateway<Empty, Empty>(
            DELETE_PROFILE_METHOD_NAME,
            empty {  },
            Empty.newBuilder(),
        )
    }
}