plugins {
    id("com.sorrowblue.android-feature")
}

android {
    namespace = "com.sorrowblue.comicviewer.feature.favorite"
    resourcePrefix("favorite")
}

dependencies {
    implementation(projects.framework.compose)
    implementation(projects.domain)
    implementation(projects.feature.favorite.common)
    implementation(projects.file)
    implementation(projects.folder)
}
