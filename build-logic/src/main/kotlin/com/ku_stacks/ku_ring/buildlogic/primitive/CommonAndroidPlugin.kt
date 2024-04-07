package com.ku_stacks.ku_ring.buildlogic.primitive

import com.android.build.gradle.BaseExtension
import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class CommonAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.getByType<BaseExtension>().apply {
            defaultConfig {
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            buildFeatures.apply {
                buildConfig = true
            }
        }
        dependencies {
            implementation(libs.library("androidx-appcompat"))
            implementation(libs.library("android-material"))
            implementation(libs.library("androidx-core-ktx"))
            implementation(libs.library("androidx-lifecycle-viewmodel-ktx"))
            implementation(libs.library("androidx-activity-ktx"))
            implementation(libs.library("androidx-lifecycle-livedata"))
            implementation(libs.library("timber"))
        }
    }
}