package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.androidTestImplementation
import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.ksp
import com.ku_stacks.ku_ring.buildlogic.dsl.kspAndroidTest
import com.ku_stacks.ku_ring.buildlogic.dsl.kspTest
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("kotlin-kapt")
            apply("com.google.devtools.ksp")
            apply("dagger.hilt.android.plugin")
        }

        dependencies {
            implementation(libs.library("hilt-navigation-compose"))
            implementation(libs.library("hilt-android"))
            ksp(libs.library("androidx-hilt-compiler"))
            ksp(libs.library("hilt-compiler"))
            testImplementation(libs.library("hilt-android-testing"))
            androidTestImplementation(libs.library("hilt-android-testing"))
            kspTest(libs.library("hilt-compiler"))
            kspAndroidTest(libs.library("hilt-compiler"))
        }
    }
}
