plugins {
    id("zveron.android.library")
    id("zveron.android.library.compose")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.zveron.design"
}

dependencies {
    implementation(libs.androidx.coreKtx)

    implementation(libs.compose.ui.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.uiToolingPreview)

    implementation(libs.coil.compose)

    debugImplementation(libs.compose.ui.uiTooling)
}