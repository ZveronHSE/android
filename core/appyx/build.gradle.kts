plugins {
    id("zveron.android.library")
    id("zveron.android.library.compose")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.zveron.appyx"
}

dependencies {
    implementation(libs.appyx.core)
}