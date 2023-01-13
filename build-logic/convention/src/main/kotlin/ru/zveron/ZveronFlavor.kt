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

private const val HOST_FIELD = "host"

enum class ZveronFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType),
    prod(FlavorDimension.contentType, ".prod"),
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

fun Project.configureFlavors(
    commonExtension: CommonExtension<*, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: ZveronFlavor) -> Unit = {}
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.contentType.name
        productFlavors {
            ZveronFlavor.values().forEach {
                create(it.name) {
                    dimension = it.dimension.name
                    flavorConfigurationBlock(this, it)

                    buildConfigField(
                        "String",
                        HOST_FIELD,
                        "\"${this@configureFlavors.getHost(it)}\"",
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