plugins {
    id("zveron.android.library")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.zveron.network"
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.retrofit)

    implementation(libs.grpc.protobuf.javaUtil)
    implementation(libs.grpc.okhttp)
    implementation(libs.zveronContracts)

    implementation(project(":core:authorization"))
}