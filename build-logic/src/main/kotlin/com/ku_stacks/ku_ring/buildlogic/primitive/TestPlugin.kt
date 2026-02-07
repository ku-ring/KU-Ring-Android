package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.androidExtension
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        androidExtension().packaging.resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE*",
                "META-INF/licenses/**",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
            )
        )

        dependencies {
            "testImplementation"(libs.findBundle("unit-test").get())
        }
    }
}
