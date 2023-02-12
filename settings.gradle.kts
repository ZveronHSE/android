pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        includeBuild("build-logic")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
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
