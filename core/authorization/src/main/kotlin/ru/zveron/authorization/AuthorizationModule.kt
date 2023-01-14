package ru.zveron.authorization

import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.zveron.authorization.base_api.TokenParser
import ru.zveron.authorization.fingerprint.FingerprintFactory
import ru.zveron.authorization.network.AuthAuthenticator
import ru.zveron.authorization.network.AuthInterceptor
import ru.zveron.authorization.network.AuthorizationTokenParserInterceptor
import ru.zveron.authorization.network.data.RefreshTokenRepository
import ru.zveron.authorization.network.data.api.RefreshTokenApi
import ru.zveron.authorization.network.domain.RefreshTokenInteractor
import ru.zveron.authorization.storage.AuthorizationPreferencesWrapper
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.authorization.storage.AuthorizationStorageImpl
import ru.zveron.core.authorization.BuildConfig

val authorizationModule = module {
    singleOf(::AuthorizationPreferencesWrapper)
    singleOf(::AuthorizationStorageImpl) bind AuthorizationStorage::class

    singleOf(::FingerprintFactory)
}

val interceptorsModule = module {
    single<RefreshTokenApi> {
        val tokenInterceptor = get<AuthorizationTokenParserInterceptor>()
        val ohHttpClient = OkHttpClient.Builder().addInterceptor(tokenInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.host)
            .client(ohHttpClient)
            .build()
        retrofit.create(RefreshTokenApi::class.java)
    }

    singleOf(::TokenParser)
    singleOf(::RefreshTokenRepository)
    singleOf(::RefreshTokenInteractor)

    singleOf(::AuthAuthenticator) {
        bind<Authenticator>()
        named("authorization")
    }
    singleOf(::AuthInterceptor) {
        bind<Interceptor>()
        named("authorization")
    }
    singleOf(::AuthorizationTokenParserInterceptor) {
        bind<Interceptor>()
        named("authorization_token")
    }
}