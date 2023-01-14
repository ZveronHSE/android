plugins {
    id("zveron.android.library")
}

android {
    namespace = "ru.zveron.authorization"
}

dependencies {
    implementation(libs.koin.android)
}
