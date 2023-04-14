plugins {
    id("zveron.android.library")
    id("zveron.android.library.compose")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.zveron.lots_card"
}

dependencies {
    implementation(libs.androidx.coreKtx)

    implementation(libs.appyx.core)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    implementation(libs.compose.ui.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.uiToolingPreview)

    implementation(libs.zveronContracts)

    implementation(libs.grpc.protobuf.javaUtil)
    implementation(libs.grpc.okhttp)

    debugImplementation(libs.compose.ui.uiTooling)

    implementation(project(":design"))
    implementation(project(":core:appyx"))
    implementation(project(":core:network"))
    implementation(project(":core:models"))
}
