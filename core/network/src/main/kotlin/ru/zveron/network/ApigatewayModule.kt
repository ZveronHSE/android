package ru.zveron.network

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apigatewayModule = module {
    singleOf(::ApigatewayDelegateImpl) bind ApigatewayDelegate::class
}