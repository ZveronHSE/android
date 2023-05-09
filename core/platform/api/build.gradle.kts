plugins {
    id("zveron.android.library")
}

android {
    namespace = "ru.zveron.platform.api"
}

dependencies {
    implementation(project(":design"))
}