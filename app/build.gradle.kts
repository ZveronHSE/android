import com.google.protobuf.gradle.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "ru.zveron"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.asProvider().get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.activityCompose)

    implementation(libs.compose.ui.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.uiToolingPreview)

    testImplementation(libs.junit.junit)
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

    protobuf(project(":protos"))
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
