plugins {
    id("zveron.android.library")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.zveron.models"
}

dependencies {
    implementation(libs.koin.android)

    implementation(libs.grpc.protobuf.javaUtil)
    implementation(libs.zveronContracts)
}