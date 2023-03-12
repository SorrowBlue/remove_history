@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("src/main")
        }

        val commonTest by getting {
            kotlin.srcDir("src/test")
        }

        val androidMain by getting {
            kotlin.srcDir("src/androidMain")
        }
    }

    jvmToolchain(11)
}

android {
    namespace = "com.sorrowblue.comicviewer.domain"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildTypes {
        getByName("release") {
            proguardFiles("consumer-rules.pro")
        }
        getByName("debug") {
            proguardFiles("consumer-rules.pro")
        }
        create("prerelease") {
            proguardFiles("consumer-rules.pro")
        }
        create("internal") {
            proguardFiles("consumer-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.framework)
    implementation(libs.kotlinx.serialization.core)
    api(libs.androidx.datastore.core)
    api(libs.androidx.paging.common)
}
