plugins {
    id("build-logic.android.library")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sorrowblue.comicviewer.bookshelf"
    resourcePrefix = "bookshelf_"
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(projects.framework)
    implementation(projects.domain)
    implementation(projects.viewer)

    implementation(libs.dagger.hilt.android.core)
    kapt(libs.dagger.hilt.android.compiler)

    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.coil)
    implementation(libs.androidx.paging.runtime.ktx)
}
