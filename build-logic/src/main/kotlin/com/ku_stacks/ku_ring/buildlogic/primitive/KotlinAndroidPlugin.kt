package com.ku_stacks.ku_ring.buildlogic.primitive

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class KotlinPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(plugins) {
            apply("kotlin-android")
        }

        tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
            kotlinOptions.jvmTarget = "17"
        }

        extensions.getByType<BaseExtension>().apply {
            (this as ExtensionAware).configure<KotlinJvmOptions> {
                jvmTarget = "17"

            }
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
