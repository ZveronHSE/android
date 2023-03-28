pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven {
            url = uri("https://jitpack.io")
        }
        includeBuild("build-logic")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
rootProject.name = "Zveron"
include(":app")
include(":protos")
include(":design")
include(":feature:authorization")
include(":core:appyx")
include(":core:network")
include(":core:authorization")
include(":feature:lots_feed")
include(":data:categories")
include(":feature:favorites")
include(":data:favorites")
include(":core:models")
