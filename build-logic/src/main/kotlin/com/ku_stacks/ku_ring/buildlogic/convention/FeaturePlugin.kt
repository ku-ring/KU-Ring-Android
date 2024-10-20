package com.ku_stacks.ku_ring.buildlogic.convention

import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.tasks.factory.AndroidUnitTest
import com.ku_stacks.ku_ring.buildlogic.dsl.configureAndroidLibrary
import com.ku_stacks.ku_ring.buildlogic.primitive.CommonAndroidPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.HiltPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.KotlinPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
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
            tasks.withType(Test::class.java).configureEach {
                enabled = project.hasProperty("isCI") && project.property("isCI") == "true"
            }
            tasks.withType(AndroidUnitTest::class.java).configureEach {
                enabled = project.hasProperty("isCI") && project.property("isCI") == "true"
            }
        }
    }
}
