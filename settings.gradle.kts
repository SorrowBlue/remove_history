enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("org.jetbrains.kotlin.plugin.parcelize") version "1.7.10"
        id("com.google.devtools.ksp") version "1.7.10-1.0.6"
        id("androidx.navigation.safeargs.kotlin") version "2.5.1"
        id("com.android.library") version "7.2.2"
        id("org.jetbrains.kotlin.android") version "1.7.10"
        id("com.android.dynamic-feature") version "7.2.2"
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "dagger.hilt.android.plugin") {
                useModule("com.google.dagger:hilt-android-gradle-plugin:2.43.2")
            }
        }
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ComicViewer"

include(":app")

include(":framework")

include(":domain")
include(":domain:interactor")

include(":data")
include(":data:database")
include(":data:remote")
include(":data:pdf_support")

include(":library")
include(":settings")
include(":bookshelf")
include(":management")
include(":viewer")


includeBuild("build-logic")
