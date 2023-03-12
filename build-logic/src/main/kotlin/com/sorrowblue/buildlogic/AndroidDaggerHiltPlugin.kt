package com.sorrowblue.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
internal class AndroidDaggerHiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                id("org.jetbrains.kotlin.kapt")
                id("dagger.hilt.android.plugin")
            }
            dependencies {
                add("implementation", libs.findLibrary("dagger-hilt-android-core").get())
                add("kapt", libs.findLibrary("dagger-hilt-android-compiler").get())
            }
            kapt {
                correctErrorTypes = true
            }
        }
    }
}
