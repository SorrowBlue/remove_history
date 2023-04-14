plugins {
    id("build-logic.android.library")
}

android {

    resourcePrefix("framework_notification")
}

dependencies {
    implementation(projects.framework)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.startup.runtime)
}
