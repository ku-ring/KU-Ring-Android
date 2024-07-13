package com.ku_stacks.ku_ring.buildlogic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.ku_stacks.ku_ring.buildlogic.dsl.configureAndroidLibrary
import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
import com.ku_stacks.ku_ring.buildlogic.dsl.version
import com.ku_stacks.ku_ring.buildlogic.primitive.CommonAndroidPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.ComposePlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.HiltPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.KotlinPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
        }

        apply<KotlinPlugin>()
        apply<CommonAndroidPlugin>()
        apply<HiltPlugin>()
        apply<ComposePlugin>()
        configureAndroidLibrary()
        extensions.configure<ApplicationExtension> {
            defaultConfig {
                versionCode = libs.version("versionCode").toInt()
                versionName = libs.version("appVersion")
            }
            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }
        }
        dependencies {
            implementation(libs.library("androidx-fragment-ktx"))
            implementation(libs.library("androidx-constraintlayout"))
            testImplementation(libs.library("androidx-fragment-testing"))
        }
    }
}
