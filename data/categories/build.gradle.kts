plugins {
    id("zveron.android.library")
}

android {
    namespace = "ru.zveron.categories"
}

dependencies {
    implementation(libs.koin.android)

    implementation(libs.zveronContracts)

    implementation(libs.grpc.protobuf.javaUtil)
    implementation(libs.grpc.okhttp)

    implementation(project(":core:network"))
}
