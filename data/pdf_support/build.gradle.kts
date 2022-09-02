plugins {
    id("com.android.dynamic-feature")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}
android {
    compileSdk = libs.versions.android.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.min.sdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules-dynamic-features.pro")
        }
    }
}

dependencies {
    implementation(projects.app)
    implementation(projects.data)
    implementation(projects.data.remote)

    implementation(libs.pdfbox.android) {
        exclude(group = "org.bouncycastle")
    }
}
