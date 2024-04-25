plugins {
    id("comicviewer.android.library")
    id("comicviewer.android.hilt")
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.sorrowblue.comicviewer.data.storage"
}

dependencies {
    implementation(projects.domain.service)
    api(projects.domain.model)
    api(projects.data.reader)

    androidTestImplementation(libs.androidx.test.ext.junitKtx)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
