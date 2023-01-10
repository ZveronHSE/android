import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id("zveron.android.library")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.zveron.network"
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.retrofit)
}