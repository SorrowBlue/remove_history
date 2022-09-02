plugins {
    id("build-logic.android.library")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sorrowblue.comicviewer.data.remote"
}

dependencies {
    implementation(projects.data)

    implementation(libs.codelibs.jcifs)
    implementation(libs.apache.commons.compress)
    implementation(libs.google.play.feature.delivery)
    implementation("com.googlecode.juniversalchardet:juniversalchardet:1.0.3")

    implementation(libs.dagger.hilt.android.core)
    kapt(libs.dagger.hilt.android.compiler)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}

kapt {
    correctErrorTypes = true
}
