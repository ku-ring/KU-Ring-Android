package com.ku_stacks.ku_ring.buildlogic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.ku_stacks.ku_ring.buildlogic.dsl.configureAndroidLibrary
import com.ku_stacks.ku_ring.buildlogic.primitive.CommonAndroidPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.HiltPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.KotlinPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class FeaturePlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
        }

        apply<KotlinPlugin>()
        apply<CommonAndroidPlugin>()
        apply<HiltPlugin>()
        configureAndroidLibrary()
        extensions.configure<LibraryExtension> {
            defaultConfig {
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
            }
        }
    }
}
