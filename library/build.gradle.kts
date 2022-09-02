plugins {
    id("build-logic.android.library")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sorrowblue.comicviewer.library"
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(projects.framework)
    implementation(projects.domain)
    implementation(projects.bookshelf)
    implementation(projects.management)

    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.coil)
    implementation(libs.google.play.feature.delivery)

    implementation(libs.dagger.hilt.android.core)
    kapt(libs.dagger.hilt.android.compiler)
}
