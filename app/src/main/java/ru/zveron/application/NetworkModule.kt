package ru.zveron.application

import com.squareup.moshi.Moshi
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.zveron.BuildConfig
import ru.zveron.contract.apigateway.ApigatewayServiceGrpc
import ru.zveron.network.cookies.CookieFactory

val networkModule = module {
    singleOf(::CookieFactory)

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        val authenticator = get<Authenticator>(named("authorization"))
        val interceptor = get<Interceptor>(named("authorization"))
        val loggingInterceptor = get<HttpLoggingInterceptor>()

        val tokenInterceptor = get<Interceptor>(named("authorization_token"))

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authenticator)
            .build()
    }

    single<Retrofit> {
        val moshi = get<Moshi>()
        val okHttpClient = get<OkHttpClient>()

        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    single<ManagedChannel> {
        ManagedChannelBuilder.forAddress(BuildConfig.host, BuildConfig.port).build()
    }

    single<ApigatewayServiceGrpc.ApigatewayServiceBlockingStub> {
        val channel = get<ManagedChannel>()
        ApigatewayServiceGrpc.newBlockingStub(channel)
    }
}

private fun getBaseUrl(): String {
    return BuildConfig.baseUrl
}