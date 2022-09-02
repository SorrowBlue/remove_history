plugins {
    id("build-logic.android.library")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sorrowblue.comicviewer.data"

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

}

dependencies {
    api(projects.domain.interactor)

    implementation(libs.kotlinx.serialization.protobuf)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.proto)
    implementation(libs.slf4j.android)
    implementation(libs.squareup.logcat)
    implementation("androidx.documentfile:documentfile:1.0.1")
    implementation(libs.google.play.feature.delivery)

    implementation(libs.dagger.hilt.android.core)
    kapt(libs.dagger.hilt.android.compiler)

    implementation(libs.bundles.androidx.room)
    ksp(libs.androidx.room.compiler)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

kapt {
    correctErrorTypes = true
}
