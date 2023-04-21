package ru.zveron.parameters.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.zveron.models.BuildConfig
import ru.zveron.parameters.data.ParametersGrpcSource
import ru.zveron.parameters.data.ParametersMockSource
import ru.zveron.parameters.data.ParametersRepository
import ru.zveron.parameters.data.ParametersRepositoryImpl
import ru.zveron.parameters.data.ParametersSource

val parametersModule = module {
    singleOf(::ParametersRepositoryImpl) bind ParametersRepository::class
    singleOf(::ParametersGrpcSource)
    singleOf(::ParametersMockSource)
    single<ParametersSource> {
        if (BuildConfig.useDebugMocks) {
            get<ParametersMockSource>()
        } else {
            get<ParametersGrpcSource>()
        }
    }
}