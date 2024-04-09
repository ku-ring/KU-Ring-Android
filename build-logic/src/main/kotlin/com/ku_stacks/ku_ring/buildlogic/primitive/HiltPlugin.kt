package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.androidTestImplementation
import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.kapt
import com.ku_stacks.ku_ring.buildlogic.dsl.kaptAndroidTest
import com.ku_stacks.ku_ring.buildlogic.dsl.kaptTest
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
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
            kaptTest(libs.library("hilt-compiler"))
            kaptAndroidTest(libs.library("hilt-compiler"))
        }
    }
}
