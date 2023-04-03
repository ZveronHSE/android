package ru.zveron.authorization.model

import ru.zveron.contract.auth.external.AuthProvider as GrpcAuthProvider

enum class AuthProvider {
    GOOGLE,
}

fun AuthProvider.toGrpcModel(): GrpcAuthProvider {
    return when (this) {
        AuthProvider.GOOGLE -> GrpcAuthProvider.GMAIL
    }
}