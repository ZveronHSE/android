package ru.zveron.application

import org.koin.dsl.module
import ru.zveron.network.networkModule

internal val appModule = module {
    includes(
        networkModule,
    )
}