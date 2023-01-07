plugins {
    id("zveron.android.library")
    id("zveron.android.library.compose")
}

android {
    namespace = "ru.zveron.authorization"
}

dependencies {
    implementation(libs.androidx.coreKtx)

    implementation(libs.appyx.core)

    implementation(libs.compose.ui.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.uiToolingPreview)

    debugImplementation(libs.compose.ui.uiTooling)

    implementation(project(":design"))
}
