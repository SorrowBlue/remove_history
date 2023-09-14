plugins {
    id("com.sorrowblue.android-feature")
}

android {
    namespace = "com.sorrowblue.comicviewer.feature.main"
    resourcePrefix("main")
}

dependencies {
    implementation(projects.framework.compose)
    implementation(projects.domain)
}
