package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.kapt
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RoomPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("kotlin-kapt")
        }

        dependencies {
            implementation(libs.library("androidx-room-ktx"))
            implementation(libs.library("androidx-room-runtime"))
            implementation(libs.library("androidx-room-rxjava3"))
            kapt(libs.library("androidx-room-compiler"))
            testImplementation(libs.library("androidx-room-testing"))
        }
    }
}
