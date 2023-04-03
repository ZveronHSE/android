package ru.zveron.platform

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val platformModule = module {
    singleOf(::SecretsHolderImpl) bind SecretsHolder::class
}