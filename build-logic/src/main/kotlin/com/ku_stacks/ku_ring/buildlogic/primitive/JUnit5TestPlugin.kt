package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
import com.ku_stacks.ku_ring.buildlogic.dsl.testRuntimeOnly
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JUnit5TestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("de.mannodermaus.android-junit5")

            dependencies {
                testImplementation(libs.library("junit5"))
                testRuntimeOnly(libs.library("junit5-engine"))
                testImplementation(libs.library("junit5-params"))
                testRuntimeOnly(libs.library("junit-vintage-engine"))
            }
        }
    }
}