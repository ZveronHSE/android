plugins {
    id("zveron.android.library")
}

android {
    namespace = "ru.zveron.categories"
}

dependencies {
    implementation(libs.zveronContracts)

    implementation(libs.grpc.protobuf.javaUtil)
    implementation(libs.grpc.okhttp)

    implementation(libs.koin.android)

    implementation(project(":core:network"))
    implementation(project(":core:models"))
}
