package ru.zveron.network

import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .build()
    }
}

private fun getBaseUrl(): String {
    return BuildConfig.host;
}