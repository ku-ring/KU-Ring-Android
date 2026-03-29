package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class Navigation3Plugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            dependencies {
                implementation(libs.library("navigation3-runtime"))
                implementation(libs.library("navigation3-ui"))
                implementation(libs.library("lifecycle-viewmodel-navigation3"))
            }
        }
}
