plugins {
    id("zveron.android.library")
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "ru.zveron.core.authorization"
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.retrofit)
    implementation(libs.okHttp.loggingInterceptor)
    implementation(libs.moshi)

    implementation(libs.zveronContracts)
    implementation(libs.grpc.protobuf.javaUtil)

    ksp(libs.moshi.codegen)
}
