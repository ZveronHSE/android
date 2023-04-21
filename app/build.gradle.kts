import org.jetbrains.kotlin.konan.properties.loadProperties
import ru.zveron.ZveronBuildType

plugins {
    id("zveron.android.application")
    id("zveron.android.application.compose")
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

        addManifestPlaceholders(
            mapOf(
                "appAuthRedirectScheme" to "ru.zveron",
            )
        )

        val localProperties = loadProperties("${project.rootDir}/local_zveron.properties")
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${localProperties.getProperty("google_auth_key")}\"")
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

    implementation(libs.grpc.okhttp)
    implementation(libs.zveronContracts)
    implementation(libs.grpc.protobuf.javaUtil)

    implementation(libs.openauth)

    testImplementation(libs.junit.junit)
    testImplementation(libs.koin.test.junit4)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.test.compose.ui.uiTest)
    debugImplementation(libs.test.compose.ui.uiTooling)
    debugImplementation(libs.test.compose.ui.uiTestManifest)

    implementation(libs.kotlinx.coroutines)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    implementation(libs.zveronContracts)

    implementation(libs.coil.compose)

    implementation(project(":core:appyx"))
    implementation(project(":core:authorization"))
    implementation(project(":core:network"))
    implementation(project(":core:platform:api"))
    implementation(project(":design"))

    implementation(project(":data:categories"))
    implementation(project(":data:favorites"))
    implementation(project(":data:image_storage"))

    implementation(project(":feature:authorization"))
    implementation(project(":feature:lots_feed"))
    implementation(project(":feature:favorites"))
    implementation(project(":feature:lot_card"))
    implementation(project(":feature:user_lots"))
    implementation(project(":feature:create_lot"))
    implementation(project(":feature:choose_item"))
    implementation(project(":feature:user_profile"))
}
