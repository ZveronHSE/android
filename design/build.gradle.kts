plugins {
    id("zveron.android.library")
    id("zveron.android.library.compose")
}

android {
    namespace = "ru.zveron.design"
}

dependencies {
    implementation(libs.androidx.coreKtx)

    implementation(libs.compose.ui.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.uiToolingPreview)

    debugImplementation(libs.compose.ui.uiTooling)
}