plugins {
    id("build-logic.android.library")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sorrowblue.comicviewer.viewer"
    resourcePrefix = "viewer_"
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(projects.framework)
    implementation(projects.domain)

    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.viewpager2)

    implementation(libs.dagger.hilt.android.core)
    kapt(libs.dagger.hilt.android.compiler)

    implementation(libs.coil)
}
