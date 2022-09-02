plugins {
    id("build-logic.android.application")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sorrowblue.comicviewer"
    defaultConfig {
        applicationId = "com.sorrowblue.comicviewer"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    dynamicFeatures += setOf(":data:pdf_support")
}

dependencies {
    implementation(projects.framework)
    implementation(projects.data)
    implementation(projects.data.remote)
    implementation(projects.data.database)
    implementation(projects.domain)
    implementation(projects.settings)
    implementation(projects.bookshelf)
    implementation(projects.library)
    implementation(projects.management)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.dagger.hilt.android.core)
    kapt(libs.dagger.hilt.android.compiler)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}

kapt {
    correctErrorTypes = true
}
