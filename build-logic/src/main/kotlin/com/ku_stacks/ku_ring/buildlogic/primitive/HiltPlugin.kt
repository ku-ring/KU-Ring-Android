package com.ku_stacks.ku_ring.buildlogic.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("kotlin-kapt")
            apply("dagger.hilt.android.plugin")
        }

        dependencies {
            implementation(libs.library("hilt-navigation-compose"))
            implementation(libs.library("hilt-android"))
            kapt(libs.library("androidx-hilt-compiler"))
            kapt(libs.library("hilt-compiler"))
            testImplementation(libs.library("hilt-android-testing"))
            androidTestImplementation(libs.library("hilt-android-testing"))
        }
    }
}
