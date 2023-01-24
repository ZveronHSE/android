plugins {
    id("zveron.android.library")
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "ru.zveron.core.authorization"
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.retrofit)
    implementation(libs.okHttp.loggingInterceptor)
    implementation(libs.moshi)

    implementation(project(":core:network"))

    ksp(libs.moshi.codegen)
}
