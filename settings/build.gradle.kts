plugins {
    id("build-logic.android.library")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sorrowblue.comicviewer.settings"
    resourcePrefix = "settings_"
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(projects.framework)
    implementation(projects.domain)

    implementation(libs.androidx.biometric)
    implementation(libs.androidx.preference.ktx)

    implementation(libs.dagger.hilt.android.core)
    kapt(libs.dagger.hilt.android.compiler)
}
