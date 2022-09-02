plugins {
    id("build-logic.android.library")
}

android {
    namespace = "com.sorrowblue.comicviewer.framework"
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    api(libs.bundles.androidx.lifecycle)
    api(libs.bundles.androidx.navigation)

    api(libs.google.material)
    api(libs.squareup.logcat)
    api(libs.androidx.recyclerview)
    api(libs.chrisbanes.insetter)
    api(libs.sorrowblue.binding.ktx)

    implementation(libs.dagger.hilt.android.core)
    kapt(libs.dagger.hilt.android.compiler)
}
