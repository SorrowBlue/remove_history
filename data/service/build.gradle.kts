plugins {
    id("build-logic.android.library")
    id("com.sorrowblue.dagger-hilt")
    id("org.jetbrains.kotlin.kapt")
}

dependencies {
    implementation(projects.framework)
    implementation(projects.framework.notification)
    implementation(projects.data)

    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.work.runtime.ktx)
}
