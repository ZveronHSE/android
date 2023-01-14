package ru.zveron.application

import com.squareup.moshi.Moshi
import org.koin.dsl.module
import ru.zveron.authorization.authorizationModule
import ru.zveron.authorization.interceptorsModule
import ru.zveron.network.networkModule

private val applicationSingletonModule = module {
    single<Moshi> {
        Moshi.Builder()
            .build()
    }
}

internal val appModule = module {
    includes(
        applicationSingletonModule,
        networkModule,
        authorizationModule,
        interceptorsModule,
    )
}
