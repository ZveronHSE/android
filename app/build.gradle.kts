import com.google.protobuf.gradle.*
import ru.zveron.ZveronBuildType

plugins {
    id("zveron.android.application")
    id("zveron.android.application.compose")
    id("com.google.protobuf")
    id("kotlin-parcelize")
}

android {
    defaultConfig {
        applicationId = "ru.zveron"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = ZveronBuildType.DEBUG.applicationIdSuffix
        }

        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = ZveronBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "ru.zveron"
}

dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.activityCompose)

    implementation(libs.compose.ui.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.uiToolingPreview)

    implementation(libs.appyx.core)

    implementation(libs.retrofit)
    implementation(libs.retrofit.moshiConverter)

    testImplementation(libs.junit.junit)
    testImplementation(libs.koin.test.junit4)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.test.compose.ui.uiTest)
    debugImplementation(libs.test.compose.ui.uiTooling)
    debugImplementation(libs.test.compose.ui.uiTestManifest)

    implementation(libs.kotlinx.coroutines)

    runtimeOnly(libs.grpc.okhttp)

    api(libs.grpc.grpcStub)
    api(libs.grpc.protobuf.lite)
    api(libs.grpc.grpcKotlinStub)
    api(libs.grpc.protobuf.kotlinLite)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    implementation(libs.moshi)

    protobuf(project(":protos"))

    implementation(project(":core:appyx"))
    implementation(project(":core:authorization"))
    implementation(project(":core:network"))
    implementation(project(":design"))

    implementation(project(":feature:authorization"))
}

protobuf {
    protoc {
        artifact = libs.grpc.protobuf.protoc.get().toString()
    }
    plugins {
        id("grpc") {
            artifact = libs.grpc.protobuf.protocJavaGen.get().toString()
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
            task.plugins {
                id("grpc") {
                    option("lite")
                }
            }
        }
    }
}
