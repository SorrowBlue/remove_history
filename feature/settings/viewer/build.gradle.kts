plugins {
    id("comicviewer.android.feature")
}

android {
    namespace = "com.sorrowblue.comicviewer.feature.settings.viewer"
}

dependencies {
    implementation(projects.framework.compose)
    implementation(projects.domain)
}
