package com.ku_stacks.ku_ring.buildlogic.convention

import com.android.build.gradle.BaseExtension
import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ViewBasedFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("kotlin-kapt") // remove when view binding is no longer used
        }

        apply<FeaturePlugin>()

        extensions.getByType<BaseExtension>().apply {
            buildFeatures.apply {
                viewBinding = true
            }
            dataBinding {
                enable = true
            }
        }
        dependencies {
            implementation(libs.library("androidx-fragment-ktx"))
            implementation(libs.library("androidx-constraintlayout"))
            testImplementation(libs.library("androidx-fragment-testing"))
        }
    }
}
