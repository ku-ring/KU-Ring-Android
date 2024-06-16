package com.ku_stacks.ku_ring.buildlogic.primitive

import com.android.build.gradle.BaseExtension
import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            with(plugins) {
                apply("kotlin-android")
            }

            tasks.withType<KotlinCompile> {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }

            extensions.getByType<BaseExtension>().apply {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }

            dependencies {
                implementation(libs.library("kotlinx-coroutines-core"))
                implementation(libs.library("kotlinx-coroutines-android"))
                implementation(libs.library("kotlinx-coroutines-reactive"))
                implementation(libs.library("kotlinx-coroutines-rxjava3"))
            }
        }
}
