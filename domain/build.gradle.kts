plugins {
    id("build-logic.android.library")
    id("org.jetbrains.kotlin.plugin.parcelize")
    kotlin("plugin.serialization") version "1.7.10"
}

android {
    namespace = "com.sorrowblue.comicviewer.domain"
}

dependencies {
    implementation(libs.kotlinx.serialization.core)
    api(libs.androidx.paging.common)
}
