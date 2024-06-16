package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.android
import com.ku_stacks.ku_ring.buildlogic.dsl.debugImplementation
import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(plugins) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
                enableStrongSkippingMode.set(true)
                includeSourceInformation.set(true)
            }

            android {
                dependencies {
                    implementation(platform(libs.library("compose-bom")))
                    implementation(libs.library("compose-foundation"))
                    implementation(libs.library("compose-ui"))
                    implementation(libs.library("compose-ui-tooling-preview"))
                    implementation(libs.library("compose-material"))
                    implementation(libs.library("compose-material3"))
                    implementation(libs.library("compose-material-icons-core"))
                    implementation(libs.library("compose-material-icons-extended"))
                    implementation(libs.library("androidx-activity-compose"))
                    implementation(libs.library("androidx-constraintlayout-compose"))
                    implementation(libs.library("androidx-lifecycle-viewmodel-compose"))
                    implementation(libs.library("androidx-lifecycle-runtime-compose"))
                    implementation(libs.library("androidx-navigation-compose"))
                    implementation(libs.library("lottie-compose"))
                    implementation(libs.library("coil-compose"))
                    testImplementation(libs.library("compose-ui-test-junit4"))
                    debugImplementation(libs.library("compose-ui-tooling"))
                    debugImplementation(libs.library("compose-ui-test-manifest"))
                }
            }
        }
}
