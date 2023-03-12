package com.sorrowblue.buildlogic

import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

fun Project.plugins(action: PluginManager.() -> Unit) {
    action(pluginManager)
}

fun PluginManager.id(name: String) = apply(name)

@Suppress("unused")
internal class AndroidApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            plugins {
                id("com.android.application")
                id("org.jetbrains.kotlin.android")
            }

            extensions.configure<BaseAppModuleExtension> {
                applyCommonConfigure(this@with)
                defaultConfig {
                    targetSdk = libs.version("android-target-sdk").toInt()
                }
            }

            kotlin {
                jvmToolchain(11)
            }
        }
    }
}

@Suppress("unused")
internal class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            plugins {
                id("com.android.library")
                id("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                this.applyCommonConfigure(this@with)
                buildTypes {
                    getByName("release") {
                        consumerProguardFiles("consumer-rules.pro")
                    }
                    getByName("debug") {
                        consumerProguardFiles("consumer-rules.pro")
                    }
                    create("prerelease") {
                        consumerProguardFiles("consumer-rules.pro")
                    }
                    create("internal") {
                        consumerProguardFiles("consumer-rules.pro")
                    }
                }
            }
            kotlin {
                jvmToolchain(11)
            }
        }
    }
}

@Suppress("unused")
internal class AndroidDynamicFeaturePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            plugins {
                id("com.android.dynamic-feature")
                id("org.jetbrains.kotlin.android")
            }
            extensions.configure<DynamicFeatureExtension> {
                this.applyCommonConfigure(this@with)
                buildTypes {
                    getByName("release") {
                        proguardFile("consumer-rules.pro")
                    }
                    getByName("debug") {
                        proguardFile("consumer-rules.pro")
                    }
                    create("prerelease") {
                        proguardFile("consumer-rules.pro")
                    }
                    create("internal") {
                        proguardFile("consumer-rules.pro")
                    }
                }
            }
            kotlin {
                jvmToolchain(11)
            }
        }
    }
}

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
fun org.gradle.api.Project.`kapt`(configure: Action<KaptExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kapt", configure)
