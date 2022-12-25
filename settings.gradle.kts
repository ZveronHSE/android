pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        includeBuild("plugins")
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
