plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
}

gradlePlugin {
    plugins {
        register("android.library") {
            id = "build-logic.android.library"
            implementationClass = "com.sorrowblue.buildlogic.AndroidLibraryPlugin"
        }
        register("android.application") {
            id = "build-logic.android.application"
            implementationClass = "com.sorrowblue.buildlogic.AndroidApplicationPlugin"
        }
    }
}
