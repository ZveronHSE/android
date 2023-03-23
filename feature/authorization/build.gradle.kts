plugins {
    id("zveron.android.library")
    id("zveron.android.library.compose")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "ru.zveron.authorization"
}

dependencies {
    implementation(libs.androidx.coreKtx)

    implementation(libs.appyx.core)

    implementation(libs.retrofit)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    implementation(libs.compose.ui.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.uiToolingPreview)

    implementation(libs.zveronContracts)
    implementation(libs.grpc.protobuf.javaUtil)

    debugImplementation(libs.compose.ui.uiTooling)

    implementation(project(":design"))
    implementation(project(":core:appyx"))
    implementation(project(":core:authorization"))
    implementation(project(":core:network"))
}
