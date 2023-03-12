@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("build-logic.android.library")
    id("com.sorrowblue.dagger-hilt")
}

dependencies {
    implementation(projects.framework)
    implementation(projects.data)
    implementation(projects.data.common)
    implementation(projects.framework.notification)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.squareup.logcat)

    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)
}
