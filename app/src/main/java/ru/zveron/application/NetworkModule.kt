package ru.zveron.application

import com.google.protobuf.util.JsonFormat
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.koin.dsl.module
import ru.zveron.BuildConfig
import ru.zveron.contract.apigateway.ApigatewayServiceGrpcKt

val networkModule = module {
    single<ManagedChannel> {
        ManagedChannelBuilder
            .forAddress(BuildConfig.host, BuildConfig.port)
            .usePlaintext()
            .intercept()
            .build()
    }

    single {
        val channel = get<ManagedChannel>()
        ApigatewayServiceGrpcKt.ApigatewayServiceCoroutineStub(channel)
    }

    single {
        JsonFormat.printer()
    }

    single {
        JsonFormat.parser()
    }
}
