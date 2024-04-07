package com.ku_stacks.ku_ring.buildlogic.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FirebasePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase:firebase-crashlytics-gradle")
            }

            dependencies {
                implementationPlatform(libs.library("firebase-bom"))
                implementation(libs.bundle("firebase"))
            }
        }
    }
}
