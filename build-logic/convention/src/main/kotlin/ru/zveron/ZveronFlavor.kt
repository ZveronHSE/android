package ru.zveron

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.konan.properties.loadProperties

enum class FlavorDimension {
    contentType
}

private const val BASE_URL_FIELD = "baseUrl"
private const val HOST_FIELD = "host"
private const val PORT_FIELD = "port"
private const val USE_DEBUG_MOCKS = "useDebugMocks"

enum class ZveronFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType),
    prod(FlavorDimension.contentType, ".prod"),
}

fun Project.getBaseUrl(zveronFlavor: ZveronFlavor): String {
    val zveronProperties = loadProperties("$rootDir/zveron.properties")

    return when (zveronFlavor) {
        ZveronFlavor.demo -> {
            val debugBaseUrl: String by zveronProperties
            debugBaseUrl
        }

        ZveronFlavor.prod -> {
            val prodBaseUrl: String by zveronProperties
            prodBaseUrl
        }
    }
}

fun Project.getHost(zveronFlavor: ZveronFlavor): String {
    val zveronProperties = loadProperties("$rootDir/zveron.properties")

    return when (zveronFlavor) {
        ZveronFlavor.demo -> {
            val debugHost: String by zveronProperties
            debugHost
        }

        ZveronFlavor.prod -> {
            val prodHost: String by zveronProperties
            prodHost
        }
    }
}

fun Project.useDebugMocks(): Boolean {
    val zveronProperties = loadProperties("$rootDir/zveron.properties")
    val useDebugMocks: String by zveronProperties
    return useDebugMocks.toBoolean()
}

fun Project.getPort(zveronFlavor: ZveronFlavor): Int {
    val zveronProperties = loadProperties("$rootDir/zveron.properties")

    val stringPortValue = when (zveronFlavor) {
        ZveronFlavor.demo -> {
            val debugPort: String by zveronProperties
            debugPort
        }

        ZveronFlavor.prod -> {
            val prodPort: String by zveronProperties
            prodPort
        }
    }

    return stringPortValue.toInt()
}

fun Project.configureFlavors(
    commonExtension: CommonExtension<*, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: ZveronFlavor) -> Unit = {}
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.contentType.name
        productFlavors {
            ZveronFlavor.values().forEach {
                create(it.name) {
                    buildConfigField(
                        "Boolean",
                        USE_DEBUG_MOCKS,
                        "${this@configureFlavors.useDebugMocks()}"
                    )

                    dimension = it.dimension.name
                    flavorConfigurationBlock(this, it)

                    buildConfigField(
                        "String",
                        BASE_URL_FIELD,
                        "\"${this@configureFlavors.getBaseUrl(it)}\"",
                    )

                    buildConfigField(
                        "String",
                        HOST_FIELD,
                        "\"${this@configureFlavors.getHost(it)}\"",
                    )

                    buildConfigField(
                        "int",
                        PORT_FIELD,
                        this@configureFlavors.getPort(it).toString(),
                    )

                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (it.applicationIdSuffix != null) {
                            this.applicationIdSuffix = it.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}