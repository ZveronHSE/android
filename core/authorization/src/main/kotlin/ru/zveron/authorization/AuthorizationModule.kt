package ru.zveron.authorization

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.zveron.authorization.fingerprint.FingerprintFactory
import ru.zveron.authorization.network.data.RefreshTokenRepository
import ru.zveron.authorization.network.domain.RefreshTokenInteractor
import ru.zveron.authorization.storage.AuthorizationPreferencesWrapper
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.authorization.storage.AuthorizationStorageImpl

val authorizationModule = module {
    singleOf(::AuthorizationPreferencesWrapper)
    singleOf(::AuthorizationStorageImpl) bind AuthorizationStorage::class

    singleOf(::FingerprintFactory)

    singleOf(::RefreshTokenRepository)
    singleOf(::RefreshTokenInteractor)
}

val interceptorsModule = module {

}