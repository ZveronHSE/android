plugins {
    id("zveron.android.library")
    id("zveron.android.library.compose")
}

android {
    namespace = "ru.zveron.personal_profile"
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

    debugImplementation(libs.compose.ui.uiTooling)

    implementation(project(":design"))
    implementation(project(":core:appyx"))
    implementation(project(":core:network"))
    implementation(project(":core:models"))
    implementation(project(":core:authorization"))
    implementation(project(":core:platform:api"))

    implementation(project(":data:favorites"))
}
