package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            "androidTestImplementation"(libs.findBundle("android-test").get())
            "androidTestImplementation"(platform(libs.findLibrary("compose-bom")))
            "androidTestImplementation"(libs.findLibrary("compose-ui-test"))
            "debugImplementation"(libs.findLibrary("compose-ui-test-manifest"))
        }
    }
}
