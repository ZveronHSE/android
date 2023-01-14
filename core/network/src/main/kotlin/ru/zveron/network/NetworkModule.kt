package ru.zveron.network

import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.zveron.network.cookies.CookieFactory

val networkModule = module {
    singleOf(::CookieFactory)

    single<OkHttpClient> {
        val authenticator = get<Authenticator>(named("authorization"))
        val interceptor = get<Interceptor>(named("authorization"))

        val tokenInterceptor = get<Interceptor>(named("authorization_token"))

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(tokenInterceptor)
            .authenticator(authenticator)
            .build()
    }

    single<Retrofit> {
        val okHttpClient = get<OkHttpClient>()

        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(okHttpClient)
            .build()
    }
}

private fun getBaseUrl(): String {
    return BuildConfig.host
}