package com.ku_stacks.ku_ring.buildlogic.primitive

import com.android.build.gradle.BaseExtension
import com.ku_stacks.ku_ring.buildlogic.dsl.androidTestImplementation
import com.ku_stacks.ku_ring.buildlogic.dsl.bundle
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.getByType<BaseExtension>().apply {
            packagingOptions {
                resources.excludes.add("META-INF/LICENSE*")
                resources.excludes.add("META-INF/licenses/**")
                resources.excludes.add("META-INF/AL2.0")
                resources.excludes.add("META-INF/LGPL2.1")
            }
        }

        dependencies {
            "testImplementation"(libs.findBundle("unit-test").get())
            // "androidTestImplementation"(libs.findBundle("android-test").get())
        }
    }
}
