package ru.zveron.personal_profile.profile_preview.data

import com.google.protobuf.Empty
import com.google.protobuf.empty
import ru.zveron.network.ApigatewayDelegate

private const val LOGOUT_METHOD_NAME = "authPerformLogout"

internal class LogoutRepository(
    private val apigatewayDelegate: ApigatewayDelegate,
) {
    suspend fun logout() {
        apigatewayDelegate.callApiGateway<Empty, Empty>(
            LOGOUT_METHOD_NAME,
            empty {  },
            Empty.newBuilder(),
        )
    }
}