@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("build-logic.android.library")
    id("com.sorrowblue.dagger-hilt")
}

android {
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    api(projects.framework)
    api(projects.framework.resource)

    api(libs.androidx.appcompat)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.coordinatorlayout)
    api(libs.androidx.core.ktx)
    api(libs.androidx.fragment.ktx)
    api(libs.bundles.androidx.lifecycle)
    api(libs.bundles.androidx.navigation)
    api(libs.google.android.play.feature.delivery.ktx)
    api(libs.androidx.recyclerview)
    api(libs.androidx.viewpager2)
    api(libs.google.material)
    api(libs.coil)
    api(libs.kotlinx.coroutines.core)
    api(libs.squareup.logcat)
    api(libs.chrisbanes.insetter)
    api(libs.sorrowblue.binding.ktx)
    api(libs.androidx.paging.runtime.ktx)
}
