package ru.zveron.application

import com.squareup.moshi.Moshi
import org.koin.dsl.module
import ru.zveron.authorization.authorizationModule
import ru.zveron.authorization.interceptorsModule
import ru.zveron.authorization.phone.rootPhoneModule
import ru.zveron.main_screen.mainScreenModule

private val applicationSingletonModule = module {
    single<Moshi> {
        Moshi.Builder().build()
    }
}

internal val appModule = module {
    includes(
        applicationSingletonModule,
        networkModule,
        authorizationModule,
        interceptorsModule,

        rootPhoneModule,
        mainScreenModule,
    )
}
