package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.bundle
import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.implementationPlatform
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
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
