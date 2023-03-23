plugins {
    id("zveron.android.library")
}

android {
    namespace = "ru.zveron.core.authorization"
}

dependencies {
    implementation(libs.koin.android)

    implementation(libs.zveronContracts)
    implementation(libs.grpc.protobuf.javaUtil)
}
