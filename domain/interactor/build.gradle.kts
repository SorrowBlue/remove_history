plugins {
    id("build-logic.android.library")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sorrowblue.comicviewer.domain.interactor"
}

dependencies {
    api(projects.domain)

    implementation(libs.dagger.hilt.android.core)
    kapt(libs.dagger.hilt.android.compiler)
}
